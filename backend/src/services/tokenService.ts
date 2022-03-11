import jwt, { JwtPayload } from "jsonwebtoken";
import { ACESS_TOKEN_SECRET, REFRESH_TOKEN_SECRET } from "../keys/secrets";
import { ObjectId } from "mongoose";

interface TokenPayload extends JwtPayload {
  _id: string | ObjectId;
}

class JWTToken {
  static generateToken(_id: ObjectId | string) {
    const accessToken = jwt.sign({ _id }, ACESS_TOKEN_SECRET, {
      expiresIn: "15m",
    });

    const refreshToken = jwt.sign({ _id }, REFRESH_TOKEN_SECRET, {
      expiresIn: "7d",
    });

    return { accessToken, refreshToken };
  }

  static verifyRefreshToken(token: string): TokenPayload {
    return <TokenPayload>jwt.verify(token, REFRESH_TOKEN_SECRET);
  }
}

export default JWTToken;
