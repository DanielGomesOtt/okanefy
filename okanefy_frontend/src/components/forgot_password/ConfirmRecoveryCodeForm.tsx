import { Button, Card, CardBody, Form, Input } from "@heroui/react"
import { useState } from "react"
import { BASE_URL } from "../../utils/constants"

interface ConfirmRecoveryCodeFormProps {
  setStep: React.Dispatch<React.SetStateAction<string>>
}

function ConfirmRecoveryCodeForm({setStep}: ConfirmRecoveryCodeFormProps) {

    const [errorMessage, setErrorMessage] = useState<string>("")
    const [disableSubmitButton, setDisableSubmitButton] = useState<boolean>(false)
    const [recoveryCode, setRecoveryCode] = useState<string>("")

    async function confirmRecoveryCode(e: any) {
        e.preventDefault();
        try {
            setDisableSubmitButton(true)
            setErrorMessage("")
            
            const userEmail = localStorage.getItem("email")
            let params = {}

            if(userEmail) {
                params = new URLSearchParams({
                    email: userEmail,
                    code: recoveryCode
                })
            }
            const response = await fetch(BASE_URL + 'confirmRecoveryCode?' + params, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json'
                },
            })

            if (response.status !== 200) {
                throw new Error("Erro ao tentar confirmar o código de recuperação.");
            }

            setStep("change password")
        } catch (error: unknown) {
            if (error instanceof Error) {
                setErrorMessage(error.message)
                setDisableSubmitButton(false)
            } else {
                console.log(error)
                setDisableSubmitButton(false)
            }
        }
    }
    return (
            <Card className=" w-4/5 md:w-3/5 lg:w-2/5">
                <CardBody>
                    <Form
                        className="w-full flex flex-col gap-4"
                        onSubmit={(e) => confirmRecoveryCode(e)}
                        >
                        <div className="w-full text-center">
                            <span className="text-red-500 text-sm">{errorMessage}</span>
                        </div>
                        <Input
                            isRequired
                            errorMessage="Informe um código válido."
                            label="Código de recuperação"
                            labelPlacement="inside"
                            name="codigo"
                            placeholder="Insira o código de recuperação"
                            type="text"
                            onChange={(e) => setRecoveryCode(e.target.value)}
                        />
                        <div className="text-center w-full">
                            <span className="text-blue-500">O código tem uma validade de 15 minutos.</span>
                        </div>
                        <Button color="primary" type="submit" className="w-full bg-green-500" disabled={disableSubmitButton}>
                            Confirmar
                        </Button>
                    </Form>
                </CardBody>
            </Card>
        )
}

export default ConfirmRecoveryCodeForm