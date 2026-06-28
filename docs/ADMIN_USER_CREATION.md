# 管理者（ADMIN_ROLE）ユーザーの手動作成手順書

本システムでは、セキュリティの観点（デフォルト認証情報の漏洩や不正アクセスの防止）から、プロダクトの初期化コード（SQLスクリプト等）に管理者ユーザーを自動登録する処理を含めていません。

本番環境や検証環境で管理者権限（`ROLE_ADMIN`）を持つユーザーを作成する場合は、本手順に従って手動でデータベースに登録を行ってください。

---

## 1. 事前準備：パスワードのハッシュ化

Spring Securityでは、パスワードが暗号化（ハッシュ化）されてデータベースに保存される必要があります。また、本システムでは `{bcrypt}` プレフィックス付きの BCrypt ハッシュを使用しています。

外部のオンラインWebツール等に本番環境用の生パスワードを入力することは**セキュリティ上の重大なリスク**となるため、ローカルの開発環境（Java）で安全にハッシュ値を生成してください。

### 手順 1-1. ハッシュ生成用テストクラスの作成
プロジェクトのテストディレクトリ内に、一時的なテストクラス `src/test/java/com/example/demo/PasswordEncoderTest.java` を作成します。

```java
package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoderTest {
    @Test
    void generateAdminHash() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        // ★ ここに設定したい安全なパスワードを入力します
        String rawPassword = "【ここに安全なパスワードを入力】";
        
        String encodedPassword = "{bcrypt}" + encoder.encode(rawPassword);
        System.out.println("\n========================================");
        System.out.println("Generated Hash:");
        System.out.println(encodedPassword);
        System.out.println("========================================\n");
    }
}
```

### 手順 1-2. テストの実行とハッシュ値の取得
ターミナルで以下のコマンドを実行してテストを単体実行します。

```bash
./mvnw test -Dtest=PasswordEncoderTest
```
CLIのバージョン違いでエラーが出たりするので、EclipseのJUnit実行でも可

**出力例：**
```text
========================================
Generated Hash:
{bcrypt}$2a$10$Xxxxxx... (生成されたハッシュ文字列)
========================================
```

> [!WARNING]  
> 出力されたハッシュ値（`{bcrypt}` から始まる文字列全体）をメモし、テスト完了後はテストクラス（`PasswordEncoderTest.java`）を削除するか、Gitのコミット対象から除外（`.gitignore` への追加等）して生パスワードがリポジトリに残らないようにしてください。

---

## 2. データベースへの登録

データベース（MySQL）に接続し、作成したハッシュ値を使用して以下のSQL文を実行します。

### SQL実行手順
データベースクライアント（MySQL Workbench, DBeaver, `mysql` CLI など）から対象データベースに対して以下のクエリを実行します。

```sql
-- ① ユーザーの登録
-- '【管理者ユーザー名】' と '【生成したハッシュ値】' をそれぞれ置き換えてください。
-- ※ auth0_sub には UNIQUE 制約があるため、UUID() 関数を用いて一意な値を設定します。
INSERT INTO users (username, password, enabled, auth0_sub)
VALUES ('admin_user', '{bcrypt}$2a$10$Xxxxxx...', 1, UUID());

-- ② 管理者権限（ROLE_ADMIN）の付与
-- ①で指定した '【管理者ユーザー名】' に対して 'ROLE_ADMIN' を付与します。
INSERT INTO authorities (username, authority)
VALUES ('admin_user', 'ROLE_ADMIN');
```

---

## 3. 動作確認

1. アプリケーションを起動します。
2. ブラウザでログイン画面（`/login`）にアクセスします。
3. 手順2で登録した「管理者ユーザー名」と、手順1でハッシュ化する前の「生のパスワード」を入力し、ログインします。
4. ログイン後、マイページ等で管理者専用機能（メニュー追加機能など）が正常に表示・利用できることを確認します。

---

## 4. 運用上のセキュリティベストプラクティス

* **共有アカウントの禁止:**
  複数の管理者メンバーがいる場合、単一の `admin` アカウントを共有するのではなく、担当者ごとに個別の管理者ユーザーを作成してください（操作ログの追跡性担保のため）。
* **パスワードの強度:**
  管理者のパスワードは12文字以上かつ英大文字・小文字・数字・記号を組み合わせた十分な強度のものにし、パスワードマネージャー等で安全に管理してください。
* **アカウントの無効化:**
  担当者の異動や退職などで管理権限が不要になった場合は、対象ユーザーの `enabled` カラムを `0`（無効化）にするか、関連レコードを速やかに削除してください。
