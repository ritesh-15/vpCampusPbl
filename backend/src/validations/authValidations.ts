interface LoginDataInterface {
  email: string;
  password: string;
}

class AuthValidation {
  static loginValidation({ email, password }: LoginDataInterface): Boolean {
    const regx =
      /([a-zA-Z0-9\.\/\_\-]+)@([a-zA-Z0-9\.\/\_\-]+)\.([a-zA-Z]){2,7}$/;

    if (!email) throw new Error("Email is required!");

    if (!password) throw new Error("Password is required!");

    if (!regx.test(email)) throw new Error("Email address is not valid!");

    if (password.length < 6)
      throw new Error("Password must be at least 6 characters long!");

    return true;
  }
}

export default AuthValidation;
