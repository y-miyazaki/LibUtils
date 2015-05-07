LibUtils
====

本ライブラリは、Androidでアプリを作る上でのユーティリティを提供します。
* Activity
* Application
* Fragment
* Connection
* Dialog
* List
* Utils
* Widget  
etc...

## Description

* com.miya38.activity  
AbstractActivity・AbstractConnectionActivityがあります。  
このクラスを継承することで自動的なメモリクリーン・コネクション回りの共通化・ライフサイクルのログを自動的にアウトプットします。  

* com.miya38.application  
*本ライブラリを使用する際には、Applicationクラスの継承元として必ずCommonApplicationクラスをextendsしてください。*  
ライフサイクルのログの自動的にアウトプット、ユーティリティの設定自動化などを行っています。  

* com.miya38.connection  
本ライブラリではVolleyを使ったコネクションを実装しています。  
そのラッパークラスであるApiRequestクラスではネットワーク通信ログの自動吐き出し、クッキー保持機能、gzip解凍、304ステータスの自動ヘッダ設定を行います。  
よく使用されるBitmapキャッシュ機能としてはディスクキャッシュ・メモリキャッシュ・ディスクメモリ共用キャッシュ・非キャッシュ等があります。  

* com.miya38.dialog  
DialogFragmentを継承したAbstractDialogFragment・AbstractConnectionDialogFragmentがあります。  
このクラスを使用することでDialogFragmentでしなければいけない処理があらかじめ実装されています。  
また、ShareDialogFragmentはSNS等への共有機能を有しています。

後は徐々に説明を追加していきます。

## Demo

nothing...

## VS. 

## Requirement

## Usage
以下の内容はほぼ必須です。必ず作成しましょう。  
* CommonApplicationクラスを継承したApplicationクラスを作成する。  
* ネットワーク通信があるアプリの場合は、AbstractVolleySettingクラスを継承したクラスを作成する。  
このクラスはシングルトンクラスで作成すること。
* ネットワーク通信があるアプリの場合は、AbstractConnectionCommonを継承したクラスを作成する。  
主にネットワーク回りのエラーハンドリング、RequestQueue等の取得先などを設定する。    

以下の内容はここの画面を作成する場合に必須です。  
* Activityを作成する。  
継承元として、必ずAbstractActivityもしくはAbstractConnectionActivityを継承しましょう。
* Fragmentを作成する。  
継承元として、必ずAbstractFragmentもしくはAbstractConnectionFragmentを継承しましょう。
* DialogFragmentを作成する。  
継承元として、必ずAbstractDialogFragmentもしくはAbstractConnectionDialogFragmentを継承しましょう。

## Install

ライブラリの依存関係は、gradleに記載していますので以下のコマンドを実行する必要があります。

gradlew copyLibs

## Contribution

## Licence

http://www.apache.org/licenses/LICENSE-2.0

## Author

[y-miyazaki](https://github.com/y-miyazaki)
