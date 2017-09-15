### Usage
#### ConfigurationSource
Implementations of the [ConfigurationSource](https://github.com/llorllale/jconfigurations/blob/d4bb5638dd35cf8a1c6d24fa4d29e37cd26186a3/src/main/java/org/jconfigurations/source/ConfigurationSource.java#L28) interface provide maps of names of configuration properties and their values. A typical implementation you would use is the [PropertiesConfigurationSource](https://github.com/llorllale/jconfigurations/blob/d4bb5638dd35cf8a1c6d24fa4d29e37cd26186a3/src/main/java/org/jconfigurations/source/PropertiesConfigurationSource.java#L30) that adapts a `Properties` object that contains your properties into a `ConfigurationSource`.

#### @Configuration
At core of this library is [@Configuration](https://github.com/llorllale/jconfigurations/blob/d4bb5638dd35cf8a1c6d24fa4d29e37cd26186a3/src/main/java/org/jconfigurations/Configuration.java#L39) which you use to mark the fields that you would like configured:

    public class MyConfig {
      @Configuration
      private String serverName;
      @Configuration
      private int maxThreads;
      @Configuration
      private File logFile;
      @Configuration
      private URL wsdlUrl;
      
      /* getters */
    }

Assume you have a `ConfigurationSource` set up with the following map entries: 

* `"serverName" -> "myhost.com"`
* `"maxThreads" -> "10"`
* `"logFile" -> "/tmp/app.log"`
* `"wsdlUrl" -> "http://some.url/service?WSDL"`

This is how you would inject the configurations:

    public static void main(String... args) throws Exception {
      final ConfigurationsSource source = /* get configurations source */;
      final MyConfig config = new MyConfig();
      new JConfigurator(source).configure(config);
      
      config.getServerName();  //"myhost.com" (a string)
      config.getMaxThreads();  //10 (an int)
      config.getLogFile();     ///tmp/app.log (a File)
      config.getWsdlUrl();     //http://some.url/service?WSDL (a URL)
    }


`jconfigurations` supports automatic *conversion* of configuration values into the following types:

* `float`
* `int`
* `String`
* `double`
* `long`
* `boolean`
* `BigDecimal`
* `BigInteger`
* `File`
* `URL`

Need to convert to a type not on this list? Implement your own custom [ConfigurationConverter](https://github.com/llorllale/jconfigurations/blob/d4bb5638dd35cf8a1c6d24fa4d29e37cd26186a3/src/main/java/org/jconfigurations/converters/ConfigurationConverter.java#L39) and specify its class type as the configuration's converter:

    @Configuration(converter = CustomTypeConverter.class)
    private CustomType myType;

#### @Required and @Name
Should some configuration be required, just mark it with `@Required`, and the `JConfigurator` will guarantee that the configuration is present in the `ConfigurationSource`, otherwise a `ConfigurationException` will be thrown:

    @Required   //marks this as a required configuration
    @Configuration
    private String someConfiguration;

If, for some reason, the configuration's name cannot be changed at the `ConfigurationSource` (eg. you don't control the properties file in a production environment) and yet you'd like to internally refactor the property's name to something better, you can use `@Name` to specify a different property name to look for in the source:

    @Name("threads")   //will look for property named 'threads' instead of 'maxConcurrentThreads'
    @Configuration
    private int maxConcurrentThreads;

#### Collections
You might face situations where you'd like to fold multiple values into a single configuration, like a `java.util.List`. This is possible to do with that you've read so far: implement your own custom `ConfigurationConverter` and specify it via `@Configuration#converter()`. 

Since this is a fairly common scenario, `jconfigurations` already ships with the [CollectionConfiguration](https://github.com/llorllale/jconfigurations/blob/d4bb5638dd35cf8a1c6d24fa4d29e37cd26186a3/src/main/java/org/jconfigurations/CollectionConfiguration.java#L44) designed for fields of types that implement the `Collection` interface. Out of the box, `jconfigurations` supports converting fields marked with `@CollectionConfiguration` that implement `List` into an `ArrayList` and `Set` into `HashSet`.

The generic parametrized types of these collections is detected as well, and of course the same types seen earlier are supported out of the box (`Float`, `Integer`, `URL`, etc.):

    @CollectionConfiguration
    private List<File> filesToProcess;   //will be assigned an ArrayList<File>

As before, if the collection is of some other generic parametrized type then you can implement your own custom `ConfigurationConverter`:

    @CollectionConfiguration(elementConverter = CustomTypeConverter.class)
    private Set<CustomType> customTypeList;    //will be assigned a HashSet<CustomType>

If you need your `Collection` field to be some other implementation (not `ArrayList` nor `HashSet`) then you can also implement your own [CollectionConfigurationConverter](https://github.com/llorllale/jconfigurations/blob/d4bb5638dd35cf8a1c6d24fa4d29e37cd26186a3/src/main/java/org/jconfigurations/converters/CollectionConfigurationConverter.java#L46) and specify that instead:

    @CollectionConfiguration(converter = ArrayDequeConverter.class)
    private ArrayDeque deque;

... where `ArrayDequeConverter` is your custom `CollectionConfigurationConverter`.

Finally, all these examples for `@CollectionConfiguration` have worked under the assumption that the property's value in the `ConfigurationSource` have been separated by a comma, eg.: `myconfig.targets = target1,target2,target3,target4`. This delimiter can easily be configured to some other character:

    @Name("myconfig.targets")
    @CollectionConfiguration(delimiter = ":")    //specifies the colon character as delimiter
    private List<String> targets;

... which will now expect the following from the source: `myconfig.targets = target1:target2:target3:target4`.

#### Maps
`jconfigurations` also provides the `@MapConfiguration`. Similar to `@CollectionConfiguration`, you can customize the `ConfigurationConverter` to use for both the keys and the values, and you can customize the delimiter character between each map-entry, as well as the key-value separator:

    @MapConfiguration(entryDelimiter = ",", keyValueSeparator = "=", converter = CustomMapConverter.class, keyConverter = CustomKeyTypeConverter.class, valueConverter = CustomValueTypeConverter.class)
    private Map<CustomKeyType, CustomValueType> map;

The `entryDelimiter` and `keyValueSeparator` used here are actually the defaults.
