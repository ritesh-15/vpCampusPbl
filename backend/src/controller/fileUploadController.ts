import { NextFunction, Request, Response } from "express";
import multer from "multer";
import path from "path";
import UploadService from "../services/uploadFileService";
import CreateHttpError from "../utils/errorHandler";


const storage = multer.diskStorage({
    filename : (req,file,cb) => {
      const filename:string =  `${Date.now()}_${file.originalname}${path.extname(file.originalname)}`
      cb(null,filename)
    },
    destination : (req,file,cb) => {
       const dir:string = path.join(path.resolve(),"src/uploads")
       cb(null,dir)
    }
})

const uploadSingle = multer({storage:storage}).single("file")

const uploadMultiple = multer({storage:storage}).array("file")

class FileUploadController{


    async uploadSingleFile(req:Request,res:Response,next:NextFunction){        
        uploadSingle(req,res, async (error) => {

            if(!req.file){
                return next(CreateHttpError.notFound("File not found!"))
            }

            if(error){
                return next(CreateHttpError.internalServerError("Internal server error!"))
            }

            const fileInfo = req.file

            const file = await new UploadService(fileInfo.path).uploadAsAvatar();

            return res.json({
                ok:true,
                file:{
                    filename:fileInfo.originalname,
                    publicId:file.public_id,
                    url:file.secure_url
                }
            })

           
           
        })
    }


    async uploadMultipleFiles(req:Request,res:Response,next:NextFunction){
        
    }
}

export default new FileUploadController()