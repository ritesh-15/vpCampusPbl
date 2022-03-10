import { UserInterface } from "./UserInterface";
import { ObjectId } from "mongoose";

interface Attachment {
  publicId: string;
  url: string;
  type: string;
}

export interface NotificationInterface {
  userId: UserInterface | ObjectId;
  title: string;
  description: string;
  createdAt: Date;
  updatedAt: Date;
  access: string;
  attachments: Attachment[];
}
