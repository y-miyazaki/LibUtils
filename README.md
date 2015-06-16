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

Leak Canaryによりメモリリークを自動的にチェックする機能も盛り込まれています。

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

## Getting Started(Gradle)
Gradle設定は以下のものをぶち込んでください。
※後でMavenにアップすることを検討します。

    dependencies {
        debugCompile project(path: ':LibUtils:library', configuration: 'debug')
        releaseCompile project(path: ':LibUtils:library', configuration: 'release')
    }

## Getting Started(Application Class)

基本的に本ライブラリを使う場合は、ライブラリのApplicationクラスを継承することが必須です。
Applicationクラスは通常であればCommonApplicationクラスを継承すればOKです。

    public class MyApplication extends CommonApplication {
    ･･･

もしライブラリを入れすぎてMethod数が65535を超えてしまう場合はCommonMultiDexApplicationクラスを継承しましょう。

    public class MyApplication extends CommonMultiDexApplication {
    ･･･

## Getting Started(Activity Class)

基本的に本ライブラリを使う場合は、AbstractActivityクラスを継承することが必須です。

    public class MyActivity extends AbstractActivity {
    ･･･

通信を行うクラスを作りたい場合は、AbstractConnectionActivityクラスを継承しましょう。

    public class MyActivity extends AbstractConnecttionActivity {
    ･･･

## Getting Started(Fragment Class)

基本的に本ライブラリを使う場合は、AbstractFragmentクラスを継承することが必須です。

    public class MyFragment extends AbstractFragment {
    ･･･

通信を行うクラスを作りたい場合は、AbstractConnectionFragmentクラスを継承しましょう。

    public class MyFragment extends AbstractConnecttionFragment {
    ･･･


## Demo

nothing...

## VS. 

## Requirement

## Usage
以下の内容はほぼ必須です。必ず作成しましょう。  
* CommonApplicationクラスもしくはCommonMultiDexApplicationクラスを継承したApplicationクラスを作成する。  
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

## Proguard

    ########## Support Library ##########
    -keep class android.support.v4.** { *; }
    -keep class android.support.v7.** { *; }
    ########## jackson ##########
    -keep class com.fasterxml.** {*; }
    ########## LibUtils ##########
    -keepnames class com.miya38.widget.**
    -keepclasseswithmembers class com.miya38.widget.NetworkImageView {*;}
    -keepclasseswithmembers class com.android.volley.toolbox.ImageLoader { *;}

## Install

## Contribution

## Licence

http://www.apache.org/licenses/LICENSE-2.0

## Author

[y-miyazaki](https://github.com/y-miyazaki)
