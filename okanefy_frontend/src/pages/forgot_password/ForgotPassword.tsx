import { useState } from "react"
import ForgotPasswordForm from "../../components/forgot_password/ForgotPasswordForm"
import HomeNavbar from "../../components/home/HomeNavbar"
import ConfirmRecoveryCodeForm from "../../components/forgot_password/ConfirmRecoveryCodeForm"
import ChangePasswordForm from "../../components/forgot_password/ChangePasswordForm"


function ForgotPassword() {

  const [step, setStep] = useState<string>("email")

  return (
    <div className="w-screen h-screen">
        <div className="h-screen flex flex-col w-full">
            <HomeNavbar />
            {step == "email" && (
              <div className="flex-1 flex flex-col mt-24 items-center gap-4">
                <img src="src/assets/esqueceu_senha_email_okanefy.png" alt="samurai e um mascote em formato de e-mail" className="w-84"/>
                <ForgotPasswordForm setStep={setStep}/>
              </div>
            )}

            {step == "confirm code" && (
              <div className="flex-1 flex flex-col mt-8 items-center gap-4">
                <img src="src/assets/codigo_recuperacao_okanefy.png" alt="samurai pensando em um código de recuperação" className="w-84"/>
                <ConfirmRecoveryCodeForm setStep={setStep}/>
              </div>
            )}

            {step == "change password" && (
              <div className="flex-1 flex flex-col mt-8 items-center gap-4">
                <img src="src/assets/mudar_senha_okanefy.png" alt="samurai mudando sua senha" className="w-84"/>
                <ChangePasswordForm setStep={setStep}/>
              </div>
            )}
        </div>
    </div>
    
  )
}

export default ForgotPassword