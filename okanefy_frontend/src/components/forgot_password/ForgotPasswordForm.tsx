import { Button, Card, CardBody, Form, Input } from "@heroui/react"
import { useState } from "react"
import { BASE_URL } from "../../utils/constants";

interface ForgotPasswordFormProps {
  setStep: React.Dispatch<React.SetStateAction<string>>
}

function ForgotPasswordForm({setStep}: ForgotPasswordFormProps) {

    const [errorMessage, setErrorMessage]= useState<string>("")
    const [email, setEmail] = useState<string>("")
    const [disableSubmitButton, setDisableSubmitButton] = useState<boolean>(false)

    async function confirmEmail(e: any) {
        e.preventDefault();
        try {
            setDisableSubmitButton(true)
            setErrorMessage("")
            const requestBody = {
                "email": email
            }

            const response = await fetch(BASE_URL + 'forgotPassword', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(requestBody)
            })

            if (response.status !== 201) {
                const errorData = await response.json();
                throw new Error(errorData.message || "Erro ao tentar confirmar o e-mail.");
            }

            const data = await response.json()

            localStorage.setItem("email", data.email)

            setStep("confirm code")
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
                    onSubmit={(e) => confirmEmail(e)}
                    >
                    <div className="w-full text-center">
                        <span className="text-red-500 text-sm">{errorMessage}</span>
                    </div>
                    <Input
                        isRequired
                        errorMessage="Informe um e-mail válido."
                        label="Email"
                        labelPlacement="inside"
                        name="email"
                        placeholder="Insira seu e-mail"
                        type="email"
                        onChange={(e) => setEmail(e.target.value)}
                    />
                    
                    <Button color="primary" type="submit" className="w-full bg-green-500" disabled={disableSubmitButton}>
                        Continuar
                    </Button>
                </Form>
            </CardBody>
        </Card>
    )
}

export default ForgotPasswordForm