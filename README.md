# Особенности реализации

* Я всё-таки реализовал интерфейс Parcelable для Post при помощи Parcelize plugin, посчитав это
  удобным, и теперь передаю его между фрагментами. К тому же это избавляет от необходимости в
  создании переменной currentPost во ViewModel и, соответственно избавляет нас от бага (описан ниже)
  без необходимости в использовании SavedStateHandle в нашей ViewModel:

> Предположим, мы находились в режиме редактирования поста и переключились на другие задачи. Наше приложение выбрасывает из памяти Android, и , по моему предположению, lifeCycle ViewModel тоже завершается. Следовательно, когда пользователь возвращается на прежнее место (в режим редактирования) и нажимает кнопку "ок", он не знает, что это уже режим добавления нового поста, поскольку ViewModel уже обнулилась и currentPost == null (соответственно в репозитории срабатывает метод addNewPost).

* Кастомная реализация [PostContentFragment]:

> " PostContentFragment и проблемы с клавиатурой и фокусом"  
> by me

Перелопатил много информации по поводу вызова клавиатуры... Судя по всему недостаточно много, она
подтягивается только в ответ на Intent.Action.SEND.

* В разметке [activity_main], несмотря на рекомендацию AndroidStudio я всё же использовал
  <fragment >, а не <androidx.fragment.app.FragmentContainerView>, как показано в вебинаре,
  поскольку со вторым случаем findNavController() выбрасывает IllegalStateException при попытке
  обработки Intent.Action.SEND после нажатия на кнопку share и затем на иконку нашего приложения в
  chooser

-> ***FIXED***
> *решение проблемы описано [здесь](https://stackoverflow.com/a/59275182)*

### снова баги

* Стоит так же отметить что в моей реализации при обработке Intent.Action.SEND внутри нашего
  приложения можно наблюдать ещё один баг:

> Нажимаем share, выбираем в chooser наше приложение, попадаем в [PostContentFragment]. Дальше интереснее:
> 1) если системно возращаемся на предыдущий экран - попадаем в [FeedFragment], снова назад - снова [FeedFragment]
> 2) если нажимаем "ок" - попадаем в актуальный, обновлённый [FeedFragment], снова назад - снова [FeedFragment], но уже неактуальный, не обновлённый

При этом если вернуться на неактуальный фрагмент, можно снова нажать поделиться и тогда новый
добавленный пост сотрет старый, т.к. ему присвоится тот же ID Я предположил, что поэтому нужно
использовать SharedViewModel (собственно, дальше последовала её реализация), однако это не помогло,
фрагменты копятся в стеке. Отложил проблему за неимением времени.

* Также при обработке Intent.Action.SEND, если не удалить текст из интента, легко поймать
  IllegalArgumentException при перевороте экрана в [PostContentFragment] со следующим логом:

> E/AndroidRuntime: FATAL EXCEPTION: main Process: ru.netology.nmedia, PID: 21332 java.lang.RuntimeException: Unable to start activity ComponentInfo{ru.netology.nmedia/ru.netology.nmedia.ui.MainActivity}: java.lang.IllegalArgumentException: Navigation action/destination ru.netology.nmedia:id/to_postContentFragment cannot be found from the current destination Destination(ru.netology.nmedia:id/postContentFragment) label=fragment_post_content class=ru.netology.nmedia.ui.PostContentFragment at android.app.ActivityThread.performLaunchActivity(ActivityThread.java:2914)... ...