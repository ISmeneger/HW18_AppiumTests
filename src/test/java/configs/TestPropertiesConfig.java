package configs;

import org.aeonbits.owner.Config;

public interface TestPropertiesConfig extends Config {

    @Key("login")
    String getLogin();

    @Key("password")
    String getPassword();
}
