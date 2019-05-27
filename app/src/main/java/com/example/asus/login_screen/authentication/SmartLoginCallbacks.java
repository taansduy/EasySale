package com.example.asus.login_screen.authentication;

import studios.codelight.smartloginlibrary.users.SmartUser;
import studios.codelight.smartloginlibrary.util.SmartLoginException;

public interface SmartLoginCallbacks extends studios.codelight.smartloginlibrary.SmartLoginCallbacks {
    void onLoginSuccess(SmartUser user);
    void onLoginFailure(SmartLoginException e);
    SmartUser doCustomLogin();
    SmartUser doCustomSignup();
}
