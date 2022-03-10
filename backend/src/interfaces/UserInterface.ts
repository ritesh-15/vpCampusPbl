import { ObjectId } from "mongoose";

export interface UserInterface {
  name: string;
  email: string;
  password: string;
  avatar: {
    publicId?: string;
    url?: string;
  };
  bio?: string;
  department?: string;
  yearOfStudy?: string;
  createdAt: Date;
  updatedAt: Date;
  _id: ObjectId;
  isActivated: boolean;
  role: "user" | "faculty" | "admin";
}
