import { Button, Card, CardBody, Form, Input } from "@heroui/react"
import { useState } from "react"
import { BASE_URL } from "../../utils/constants"
import { useNavigate } from "react-router"

interface ChangePasswordFormProps {
  setStep: React.Dispatch<React.SetStateAction<string>>
}

function ChangePasswordForm({setStep}: ChangePasswordFormProps) {

    const [errorMessage, setErrorMessage]= useState<string>("")
    const [disableSubmitButton, setDisableSubmitButton] = useState<boolean>(false)
    const [password, setPassword] = useState<string>("")
    const [confirmPassword, setConfirmPassword] = useState<string>("")
    const [submitted, setSubmitted] = useState(false);
    const navigate = useNavigate()
    const passwordsMatch = password === confirmPassword

    async function changePassword(e: any) {
        e.preventDefault();
        try {

            setSubmitted(true)

            if(!passwordsMatch) {
                return
            }

            
            setDisableSubmitButton(true)
            setErrorMessage("")
            const requestBody = {
                "email": localStorage.getItem("email"),
                "password": password
            }

            const response = await fetch(BASE_URL + 'updatePassword', {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(requestBody)
            })

            if (response.status !== 204) {
                throw new Error("Erro ao tentar atualizar a senha.");
            }

            localStorage.removeItem("email")
            navigate("/")
            
            
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
                    onSubmit={(e) => changePassword(e)}
                    >
                    <div className="w-full text-center">
                        <span className="text-red-500 text-sm">{errorMessage}</span>
                    </div>
                    <Input
                        isRequired
                        errorMessage="Informe uma senha de no mínimo 8 caracteres."
                        label="Senha"
                        labelPlacement="inside"
                        name="password"
                        placeholder="Insira uma senha"
                        type="password"
                        minLength={8}
                        onChange={(e) => setPassword(e.target.value)}
                    />

                    <Input
                        isRequired
                        errorMessage={
                            submitted && !passwordsMatch ? "As senhas não coincidem" : ""
                        }
                        label="Confirmar Senha"
                        labelPlacement="inside"
                        name="confirmPassword"
                        placeholder="Confirme sua senha"
                        type="password"
                        minLength={8}
                        isInvalid={submitted && !passwordsMatch}
                        onChange={(e) => setConfirmPassword(e.target.value)}
                    />
                    <Button color="primary" type="submit" className="w-full bg-green-500" disabled={disableSubmitButton}>
                        Salvar
                    </Button>
                </Form>
            </CardBody>
        </Card>
    )
}

export default ChangePasswordForm