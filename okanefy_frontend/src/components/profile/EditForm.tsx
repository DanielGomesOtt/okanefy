import { Button, Card, CardBody, Form, Input } from "@heroui/react"
import { useState } from "react"
import { AiOutlineEye, AiOutlineEyeInvisible } from "react-icons/ai"


function EditForm() {
  const [isVisiblePassword, setIsVisiblePassword] = useState(false)
  const [typeInputPassword, setTypeInputPassword] = useState("password")
  const [isVisibleConfirmPassword, setIsVisibleConfirmPassword] = useState(false)
  const [typeInputConfirmPassword, setTypeInputConfirmPassword] = useState("password")


  function changeInputPassword() {
    if(typeInputPassword == "password") {
      setIsVisiblePassword(true)
      setTypeInputPassword("text")
    } else {
      setIsVisiblePassword(false)
      setTypeInputPassword("password")
    }
  }

  function changeInputConfirmPassword() {
    if(typeInputConfirmPassword == "password") {
      setIsVisibleConfirmPassword(true)
      setTypeInputConfirmPassword("text")
    } else {
      setIsVisibleConfirmPassword(false)
      setTypeInputConfirmPassword("password")
    }
  }
  return (
    <Card className="w-1/5">
        <CardBody>
            <Form
                className="w-full flex flex-col gap-4"
                >
                
                <Input
                    isRequired
                    errorMessage="Informe um nome."
                    label="Nome"
                    labelPlacement="inside"
                    name="nome"
                    placeholder="Insira seu nome"
                    type="text"
                    minLength={2}
                />

                <Input
                    isRequired
                    errorMessage="Informe um e-mail válido."
                    label="Email"
                    labelPlacement="inside"
                    name="email"
                    placeholder="Insira seu e-mail"
                    type="email"
                    
                 />

                <Input
                    isRequired
                    errorMessage="Informe uma senha que tenha 8 caracteres ou mais."
                    label="Senha"
                    labelPlacement="inside"
                    name="password"
                    placeholder="Informe uma senha"
                    type={typeInputPassword}
                    minLength={8}
                    endContent={
                        <Button
                        isIconOnly
                        variant="light"
                        onPress={changeInputPassword}
                        >
                        {isVisiblePassword ? (
                            <AiOutlineEye size={20} className="text-default-500" />
                        ) : (
                            <AiOutlineEyeInvisible size={20} className="text-default-500" />
                        )}
                        </Button>
                    }
                />

                <Input
                    isRequired
                    errorMessage="A senha deve ser igual a que foi preenchida no campo anterior"
                    label="Confirme a Senha"
                    labelPlacement="inside"
                    name="confirm_password"
                    placeholder="Informe a senha novamente"
                    type={typeInputConfirmPassword}
                    minLength={8}
                    endContent={
                        <Button
                        isIconOnly
                        variant="light"
                        onPress={changeInputConfirmPassword}
                        >
                        {isVisibleConfirmPassword ? (
                            <AiOutlineEye size={20} className="text-default-500" />
                        ) : (
                            <AiOutlineEyeInvisible size={20} className="text-default-500" />
                        )}
                        </Button>
                    }
                />
                
                <Button color="primary" type="submit" className="w-full bg-green-500">
                    Salvar
                </Button>
                <Button variant="flat" className="w-full bg-red-500 text-white">
                    Mudar senha
                </Button>
                
            </Form>
        </CardBody>
    </Card>
  )
}

export default EditForm