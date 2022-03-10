import { Response } from "express";
import { UserInterface } from "../interfaces/UserInterface";
import Token from "../model/tokenModel";
import JWTToken from "./tokenService";

export const setResponseToken = async (
  res: Response,
  isUser: UserInterface
) => {
  const { accessToken, refreshToken } = JWTToken.generateToken(isUser._id);

  // store refresh token in database
  await Token.create({ userId: isUser._id, token: refreshToken });

  res.cookie("accessToken", accessToken, {
    httpOnly: true,
    expires: new Date(Date.now() + 1000 * 60 * 15),
    secure: false,
  });

  res.cookie("refreshToken", refreshToken, {
    httpOnly: true,
    expires: new Date(Date.now() + 1000 * 60 * 60 * 24 * 7),
    secure: false,
  });

  return {
    accessToken, refreshToken
  }
};
