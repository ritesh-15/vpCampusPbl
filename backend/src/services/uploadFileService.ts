import { v2 as cloudinary, ConfigOptions } from "cloudinary";
import {
  CLOUDINARY_API_KEY,
  CLOUDINARY_API_SECRET,
  CLOUD_NAME,
} from "../keys/secrets";

class UploadService {
  private config: ConfigOptions;
  file: string;

  constructor(_file: string) {
    this.config = cloudinary.config({
      cloud_name: CLOUD_NAME,
      api_key: CLOUDINARY_API_KEY,
      api_secret: CLOUDINARY_API_SECRET,
    });

    this.file = _file;
  }

  async uploadAsAvatar() {
    return await cloudinary.uploader.upload(this.file, {
      upload_preset: "vp_campus",
      transformation: [{ width: 150, height: 150, crop: "fill" }],
    });
  }

  static async deletePreviousAvatar(publicId: string) {
    return await cloudinary.uploader.destroy(publicId);
  }
}

export default UploadService;
