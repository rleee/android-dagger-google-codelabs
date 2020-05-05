# Dagger with google-codelabs
App structure
- Ui
  - Registration + ViewModel
    - Enter Details + ViewModel
    - Terms and conditions
  - Main + ViewModel
  - Login + ViewModel
  - Settings + ViewModel
- User
  - UserManager
  - UserRepository
- Storage
  - SharedPreferences
  
---
  
### Step 1
Using:
- @Component
  - @Component.Factory
  - @BindsInstance
- @Module
  - @Binds
- @Inject

Start with Registration, the general `@Inject` flow will be like (except context wont need Inject):
> registration property `<--` registrationViewModel `<--` userManager `<--` sharedPreferences `<--` context


By Dagger we will need to provide Component, Module, and Inject annotation
> [registration property](#registration-activity) `<--` [MyApplication](#myapplication) `<--` [AppComponent + context](#appcomponent) `<--` [StorageModule](#storagemodule) `<--` [sharedPreferences](#sharedpreferences)

##### Registration Activity
```kotlin 
class RegistrationActivity : AppCompatActivity() {

    @Inject // <-- Injecting
    lateinit var registrationViewModel: RegistrationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {

        (application as MyApplication).appComponent.injectTo(this) // <-- MyApplication as the base applicatipn class declared in AndroidManifest
        ...
    }
    ...
}
```

##### MyApplication
a global application base class to hold the global dependency (by app)
```kotlin
open class MyApplication : Application() {

    val appComponent: AppComponent by lazy {
        DaggerAppComponent.factory().create(applicationContext) // <-- using Component.Factory to instantiate AppComponent because we need a parameter, in this case context
    }
}
```

##### AppComponent
Will use `@Component.Factory` to instantiate AppComponent to receive parameters, because one of the dependecy (sharedPreferences) need context property <br>
We will need @BindsIntance for the context, so inside dagger this variable can be used for sharedPreferences / dependency that need it
```kotlin
@Component(modules = [StorageModule::class])
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent // <-- Instantiate AppComponent using Factory to receive parameters
    }

    fun injectTo(activity: RegistrationActivity)
}
```

##### StorageModule
```kotlin
@Module
abstract class StorageModule {
    @Binds
    abstract fun bindsStorage(storage: SharedPreferencesStorage): Storage
}
```

##### SharedPreferences
See here we need context as parameter, thats why we need to pass context in AppComponent above for this use
```kotlin
class SharedPreferencesStorage
@Inject constructor(context: Context) : Storage {

    private val sharedPreferences = context.getSharedPreferences("Dagger", Context.MODE_PRIVATE)
    ...
}
```

##### dagger generated AppComponent
Checkout below code:
1. @BindsInstance will create a property
2. Factory will receive the parameter and pass to above 'context'
3. SharedPreferences will use the context

```java
public final class DaggerAppComponent implements AppComponent {
  private final Context context; // (1)

  private DaggerAppComponent(Context contextParam) {
    this.context = contextParam;
  }

  public static AppComponent.Factory factory() {
    return new Factory();
  }

  private SharedPreferencesStorage getSharedPreferencesStorage() {
    return new SharedPreferencesStorage(context);} // (3)

  ...

  private static final class Factory implements AppComponent.Factory {
    @Override
    public AppComponent create(Context context) { // (2)
      Preconditions.checkNotNull(context);
      return new DaggerAppComponent(context);
    }
  }
}
```









