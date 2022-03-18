import sgmail from "@sendgrid/mail"
import { SEND_GRID_API_KEY, SEND_GRID_EMAIL } from "../keys/secrets"

interface EmailDataInterface{
    to:string
    subject:string
    html:string
    text?:string
}

class EmailService{
    private to:string
    private subject:string
    private html:string
    private text?:string

    constructor({to,subject,html,text}:EmailDataInterface){
        this.to = to;
        this.subject = subject;
        this.html = html
        this.text = text
    }

    async send(){
        sgmail.setApiKey(SEND_GRID_API_KEY)

        const options = {
            to : this.to,
            from : {
                name:"VpCampus",
                email:SEND_GRID_EMAIL
            },
            subject : this.subject,
            text : this.text,
            html : this.html
        }

        return await sgmail.send(options)

    }

    static generateOtpTemplate(name:string,otp:number):string{
        return `<div style="font-family: Helvetica,Arial,sans-serif;min-width:1000px;overflow:auto;line-height:2">
            <div style="margin:50px auto;width:70%;padding:20px 0">
            <div style="border-bottom:1px solid #eee">
            <a href="" style="font-size:1.4em;color: #000814;text-decoration:none;font-weight:600">Vp Campus</a>
            </div>
            <p style="font-size:1.1em">Hi, ${name}</p>
            <p>You one time password for accoutn verification is given below. Please do not share this otp with anyone. This otp is valid only for 2 minitus</p>
            <h2 style="background: #000814;margin: 0 auto;width: max-content;padding: 0 10px;color: #fff;border-radius: 4px;">${otp}</h2>
            <p style="font-size:0.9em;">Regards,<br />VpCampus</p>
            <hr style="border:none;border-top:1px solid #eee" />
            <div style="float:right;padding:8px 0;color:#aaa;font-size:0.8em;line-height:1;font-weight:300">
          </div>
        </div>
      </div>`
    }
}

export default EmailService