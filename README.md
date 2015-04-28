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

## Install

ライブラリの依存関係は、gradleに記載していますので以下のコマンドを実行する必要があります。

gradlew copyLibs

## Contribution

## Licence

http://www.apache.org/licenses/LICENSE-2.0

## Author

[y-miyazaki](https://github.com/y-miyazaki)
