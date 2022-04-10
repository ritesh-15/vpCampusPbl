import { UserInterface } from "./UserInterface";
import { ObjectId } from "mongoose";

export interface ClubInterface {
  name: string;
  description: string;
  admin: UserInterface | ObjectId;
  createdAt: Date;
  updatedAt: Date;
  avatar: {
    url: String;
    publicId: String;
  };
  _id: ObjectId;
}
