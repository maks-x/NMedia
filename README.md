### найден баг

> Предположим, мы находились в режиме редактирования поста и переключились на другие задачи. Наше приложение выбрасывает из памяти Android, и , по моему предположению, lifeCycle ViewModel тоже завершается. Следовательно, когда пользователь возвращается на прежнее место (в режим редактирования) и нажимает кнопку "ок", он не знает, что это уже режим добавления нового поста, поскольку ViewModel уже обнулилась и currentPost == null (соответственно в репозитории срабатывает метод addNewPost).

# Особенности реализации

* Я всё-таки реализовал интерфейс Parcelable для Post при помощи Parcelize plugin, посчитав это
  удобным, и теперь передаю его между фрагментами. К тому же это избавляет от необходимости в
  создании переменной currentPost во ViewModel и, соответственно избавляет нас от найденного бага
  без необходимости в использовании SavedStateHandle в нашей ViewModel:

* Кастомная реализация [PostContentFragment]:

> " PostContentFragment и проблемы с клавиатурой и фокусом"  
> by me

Перелопатил много информации по поводу вызова клавиатуры... Судя по всему недостаточно много, она
подтягивается только в ответ на Intent.Action.SEND.

* В разметке [activity_main], несмотря на рекомендацию AndroidStudio я всё же использовал <fragment>
  , а не <androidx.fragment.app.FragmentContainerView>, как показано в вебинаре, поскольку со вторым
  случаем findNavController() выбрасывает IllegalStateException при попытке обработки
  Intent.Action.SEND после нажатия на кнопку share и затем на иконку нашего приложения в chooser (
  Logcat ниже)

> 2022-07-02 18:27:16.637 25188-25188/ru.netology.nmedia E/AndroidRuntime: FATAL EXCEPTION: main Process: ru.netology.nmedia, PID: 25188 java.lang.RuntimeException: Unable to start activity ComponentInfo{ru.netology.nmedia/ru.netology.nmedia.ui.MainActivity}: java.lang.IllegalStateException: Activity ru.netology.nmedia.ui.MainActivity@bf2a968 does not have a NavController set on 2131230932 at android.app.ActivityThread.performLaunchActivity(ActivityThread.java:2914)
at android.app.ActivityThread.handleLaunchActivity(ActivityThread.java:3049)
at android.app.servertransaction.LaunchActivityItem.execute(LaunchActivityItem.java:78)
at android.app.servertransaction.TransactionExecutor.executeCallbacks(TransactionExecutor.java:108)
at android.app.servertransaction.TransactionExecutor.execute(TransactionExecutor.java:68)
at android.app.ActivityThread$H.handleMessage(ActivityThread.java:1809)
at android.os.Handler.dispatchMessage(Handler.java:106)
at android.os.Looper.loop(Looper.java:193)
at android.app.ActivityThread.main(ActivityThread.java:6692)
at java.lang.reflect.Method.invoke(Native Method)
at com.android.internal.os.RuntimeInit$MethodAndArgsCaller.run(RuntimeInit.java:493)
at com.android.internal.os.ZygoteInit.main(ZygoteInit.java:858)
Caused by: java.lang.IllegalStateException: Activity ru.netology.nmedia.ui.MainActivity@bf2a968 does not have a NavController set on 2131230932 at androidx.navigation.Navigation.findNavController(Navigation.kt:50)
at androidx.navigation.ActivityKt.findNavController(Activity.kt:31)
at ru.netology.nmedia.ui.MainActivity.onCreate(MainActivity.kt:26)
at android.app.Activity.performCreate(Activity.java:7140)
at android.app.Activity.performCreate(Activity.java:7131)
at android.app.Instrumentation.callActivityOnCreate(Instrumentation.java:1272)
at android.app.ActivityThread.performLaunchActivity(ActivityThread.java:2894)
at android.app.ActivityThread.handleLaunchActivity(ActivityThread.java:3049)  at android.app.servertransaction.LaunchActivityItem.execute(LaunchActivityItem.java:78)  at android.app.servertransaction.TransactionExecutor.executeCallbacks(TransactionExecutor.java:108)  at android.app.servertransaction.TransactionExecutor.execute(TransactionExecutor.java:68)  at android.app.ActivityThread$H.handleMessage(ActivityThread.java:1809)  at android.os.Handler.dispatchMessage(Handler.java:106)  at android.os.Looper.loop(Looper.java:193)  at android.app.ActivityThread.main(ActivityThread.java:6692)  at java.lang.reflect.Method.invoke(Native Method)  at com.android.internal.os.RuntimeInit$MethodAndArgsCaller.run(RuntimeInit.java:493)  at com.android.internal.os.ZygoteInit.main(ZygoteInit.java:858) 

### снова баги

* Стоит так же отметить что в моей реализации при обработке Intent.Action.SEND внутри нашего
  приложения можно наблюдать ещё один баг:

> Нажимаем share, выбираем в chooser наше приложение, попадаем в [PostContentFragment]. Дальше интереснее:
> 1) если системно возращаемся на предыдущий экран - попадаем в [FeedFragment], снова назад - снова [FeedFragment]
> 2) если нажимаем "ок" - попадаем в актуальный, обновлённый [FeedFragment], снова назад - снова [FeedFragment], но уже неактуальный, не обновлённый

Я предположил, что поэтому нужно использовать SharedViewModel (собственно, дальше последовала её
реализация), однако это не помогло, фрагменты копятся в стеке. Отложил проблему за неимением времени.

* Также при обработке Intent.Action.SEND, если не удалить текст из интента, легко поймать
  IllegalArgumentException при перевороте экрана в [PostContentFragment] со следующим логом:

> 2022-07-02 19:39:07.384 21332-21332/ru.netology.nmedia E/AndroidRuntime: FATAL EXCEPTION: main Process: ru.netology.nmedia, PID: 21332 java.lang.RuntimeException: Unable to start activity ComponentInfo{ru.netology.nmedia/ru.netology.nmedia.ui.MainActivity}: java.lang.IllegalArgumentException: Navigation action/destination ru.netology.nmedia:id/to_postContentFragment cannot be found from the current destination Destination(ru.netology.nmedia:id/postContentFragment) label=fragment_post_content class=ru.netology.nmedia.ui.PostContentFragment at android.app.ActivityThread.performLaunchActivity(ActivityThread.java:2914)
at android.app.ActivityThread.handleLaunchActivity(ActivityThread.java:3049)
at android.app.ActivityThread.handleRelaunchActivityInner(ActivityThread.java:4785)
at android.app.ActivityThread.handleRelaunchActivity(ActivityThread.java:4694)
at android.app.servertransaction.ActivityRelaunchItem.execute(ActivityRelaunchItem.java:69)
at android.app.servertransaction.TransactionExecutor.executeCallbacks(TransactionExecutor.java:108)
at android.app.servertransaction.TransactionExecutor.execute(TransactionExecutor.java:68)
at android.app.ActivityThread$H.handleMessage(ActivityThread.java:1809)
at android.os.Handler.dispatchMessage(Handler.java:106)
at android.os.Looper.loop(Looper.java:193)
at android.app.ActivityThread.main(ActivityThread.java:6692)
at java.lang.reflect.Method.invoke(Native Method)
at com.android.internal.os.RuntimeInit$MethodAndArgsCaller.run(RuntimeInit.java:493)
at com.android.internal.os.ZygoteInit.main(ZygoteInit.java:858)
Caused by: java.lang.IllegalArgumentException: Navigation action/destination ru.netology.nmedia:id/to_postContentFragment cannot be found from the current destination Destination(ru.netology.nmedia:id/postContentFragment) label=fragment_post_content class=ru.netology.nmedia.ui.PostContentFragment at androidx.navigation.NavController.navigate(NavController.kt:1540)
at androidx.navigation.NavController.navigate(NavController.kt:1472)
at androidx.navigation.NavController.navigate(NavController.kt:1930)
at ru.netology.nmedia.ui.MainActivity.onCreate(MainActivity.kt:23)
at android.app.Activity.performCreate(Activity.java:7140)
at android.app.Activity.performCreate(Activity.java:7131)
at android.app.Instrumentation.callActivityOnCreate(Instrumentation.java:1272)
at android.app.ActivityThread.performLaunchActivity(ActivityThread.java:2894)
at android.app.ActivityThread.handleLaunchActivity(ActivityThread.java:3049)  at android.app.ActivityThread.handleRelaunchActivityInner(ActivityThread.java:4785)  at android.app.ActivityThread.handleRelaunchActivity(ActivityThread.java:4694)  at android.app.servertransaction.ActivityRelaunchItem.execute(ActivityRelaunchItem.java:69)  at android.app.servertransaction.TransactionExecutor.executeCallbacks(TransactionExecutor.java:108)  at android.app.servertransaction.TransactionExecutor.execute(TransactionExecutor.java:68)  at android.app.ActivityThread$H.handleMessage(ActivityThread.java:1809)  at android.os.Handler.dispatchMessage(Handler.java:106)  at android.os.Looper.loop(Looper.java:193)  at android.app.ActivityThread.main(ActivityThread.java:6692)  at java.lang.reflect.Method.invoke(Native Method)  at com.android.internal.os.RuntimeInit$MethodAndArgsCaller.run(RuntimeInit.java:493)  at com.android.internal.os.ZygoteInit.main(ZygoteInit.java:858) 


