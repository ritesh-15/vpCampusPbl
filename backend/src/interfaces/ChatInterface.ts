import mongoose from "mongoose";
import { ClubInterface } from "./ClubInterface";
import { UserInterface } from "./UserInterface";

export interface ChatInterface {
  clubId: ClubInterface | mongoose.ObjectId;
  message: string;
  createdAt: Date;
  _id: mongoose.ObjectId | string;
  userId: UserInterface | mongoose.ObjectId;
  updatedAt: Date;
}
