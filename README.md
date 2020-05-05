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

---
### Step 2
Using:
- Scope
- Subcomponent

Add scoping to the components, for Global Singleton we will use at `AppComponent` and `UserManager`, <br>
so wherever we instantiate the component it will be its scope <br>
> AppComponent instatiated at MyApplication, as long as MyApplication still there, the AppComponent instace will still the same

will also scope ViewModel by **Activity scope**, but before that will need to refactor AppComponent to have **Subcomponents** <br>
> AppComponent `<--` AppSubComponents `<--` RegistrationComponent

Then to use Subcomponent and ActivityScope
> registrationComponent property `<--` Activity `<--` MyApplication `<--` AppComponent `<--` RegistrationComponent

##### RegistrationActivity
As long as the Activity lives the ViewModel will live here in the Activity Scope
```kotlin
class RegistrationActivity : AppCompatActivity() {

    lateinit var registrationComponent: RegistrationComponent

    @Inject
    lateinit var registrationViewModel: RegistrationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {

        registrationComponent = (application as MyApplication).appComponent.registrationComponent().create() // <-- manually create here
        registrationComponent.injectTo(this)

        super.onCreate(savedInstanceState)
        ...
    }
}
```

##### AppComponent
```kotlin
@Singleton // <-- Singleton Scope
@Component(modules = [StorageModule::class, AppSubComponents::class]) // <-- Declare subcomponent
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }

    fun registrationComponent(): RegistrationComponent.Factory // <-- RegistrationComponent

    fun injectTo(activity: MainActivity)
}
```

##### AppSubComponents
```kotlin
@Module(subcomponents = [RegistrationComponent::class]) // <-- can add more
class AppSubComponents
```

##### RegistrationComponent
```kotlin
@ActivityScope // <-- declare the Scope
@Subcomponent
interface RegistrationComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): RegistrationComponent
    }

    fun injectTo(activity: RegistrationActivity)
    fun injectTo(fragment: EnterDetailsFragment)
    fun injectTo(fragment: TermsAndConditionsFragment)
}
```

##### ActivityScope
This scope is just a maker as long as the component mark as this scope still available in memory it wont be re-created <br>
in this case Subcomponent mark with this and is instantiated in Activity <br>
as long as the activity lives, the component will also there
```kotlin
@Scope
@MustBeDocumented
@Retention(value = AnnotationRetention.RUNTIME)
annotation class ActivityScope
```








