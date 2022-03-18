import { Request } from "express";
import { PassportStatic } from "passport";
import { Strategy as JwtStrategy } from "passport-jwt";
import { ACESS_TOKEN_SECRET } from "../keys/secrets";
import User from "../model/userModel";

const extractTokenFromCookie = (req: Request) => {
  // if token not in cookies the search in headers
  let { accessToken } = req.cookies;

  if(!accessToken){
     const token = req.headers['authorization']

     accessToken = token?.split(" ")[1]
  }

  return accessToken;
};

export const passportJwt = (passport: PassportStatic) => {
  passport.use(
    new JwtStrategy(
      {
        jwtFromRequest: extractTokenFromCookie,
        secretOrKey: ACESS_TOKEN_SECRET,
      },
      async (payload, done) => {
        const _id = payload._id;

        try {
          const user = await User.findOne({ _id });

          if (!user) return done(null, false);

          done(null, user);
        } catch (error) {
          done(error, false);
        }
      }
    )
  );
};
