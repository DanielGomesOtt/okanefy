import { Button, Card, CardBody, Form, Input } from "@heroui/react"
import { useState } from "react"
import { AiOutlineCheckCircle, AiOutlineCloseCircle, AiOutlineEye, AiOutlineEyeInvisible } from "react-icons/ai"
import { BASE_URL } from "../../utils/constants"
import { spawn } from "child_process"


function EditForm() {
  const [isVisiblePassword, setIsVisiblePassword] = useState(false)
  const [typeInputPassword, setTypeInputPassword] = useState("password")
  const [isVisibleConfirmPassword, setIsVisibleConfirmPassword] = useState(false)
  const [typeInputConfirmPassword, setTypeInputConfirmPassword] = useState("password")
  const [isHiddenComponent, setIsHiddenComponent] = useState(true)
  const [name, setName] = useState(localStorage.getItem('name'))
  const [email, setEmail] = useState(localStorage.getItem('email'))
  const [password, setPassword] = useState('')
  const [confirmPassword, setConfirmPassword] = useState('')
  const [errorMessage, setErrorMessage] = useState('')
  const [submitted, setSubmitted] = useState(false)
  const passwordsMatch = password === confirmPassword
  const [disableSubmitButton, setDisableSubmitButton] = useState(false)
  const [successMessage, setSuccessMessage] = useState('')

  async function updateUser(e: any) {
    e.preventDefault();
    try {
      setErrorMessage('')
      setSubmitted(true)

      if(!passwordsMatch) {
          return
      }
      
      setDisableSubmitButton(true)

      const requestBody = {
        "id": localStorage.getItem('id'),
        "name": name,
        "email": email,
        "password": password
      }

      const response = await fetch(BASE_URL + 'users', {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': 'Bearer ' + localStorage.getItem('token')
        },
        body: JSON.stringify(requestBody)
      })

      if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message || "Erro ao atualizar dados do usuário");
      }

      const data = await response.json()
      
      localStorage.setItem("id", data.id)
      localStorage.setItem("name", data.name)
      localStorage.setItem("email", data.email)
      if(data.token != null) {
        localStorage.setItem("token", data.token)
      }
      
      setSuccessMessage('Dados salvos com sucesso.')

      setTimeout(() => {
        setIsHiddenComponent(true)
        setPassword('')
        setConfirmPassword('')
        setTypeInputPassword('password')
        setTypeInputConfirmPassword('password')
        setIsVisiblePassword(false)
        setIsVisibleConfirmPassword(false)
        setDisableSubmitButton(false)
        setSuccessMessage('')
      }, 3000)
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

  function hiddenInputPassword () {
    setIsHiddenComponent(!isHiddenComponent)
    setPassword('')
    setConfirmPassword('')
    setTypeInputPassword('password')
    setTypeInputConfirmPassword('password')
    setIsVisiblePassword(false)
    setIsVisibleConfirmPassword(false)
  }


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
    <Card className=" w-4/5 md:w-3/5 lg:w-2/5">
        <CardBody>
            <Form
                className="w-full flex flex-col gap-4"
                onSubmit={(e) => updateUser(e)}
            >
                {errorMessage.length > 0 && (
                  <div className="text-center w-full flex justify-center items-center gap-2">
                    <AiOutlineCloseCircle size={22} className="text-red-500" />
                    <span className="text-red-500">{errorMessage}</span>
                  </div>
                )}

                {successMessage.length > 0 && (
                  <div className="text-center w-full flex justify-center items-center gap-2">
                    <AiOutlineCheckCircle size={20} className="text-green-500"/>
                    <span className="text-green-500">{successMessage}</span>
                  </div>
                )}
                <Input
                    errorMessage="Informe um nome."
                    label="Nome"
                    labelPlacement="inside"
                    name="nome"
                    placeholder="Insira seu nome"
                    type="text"
                    minLength={2}
                    value={name ? name : ''}
                    onChange={(e) => setName(e.target.value)}
                />

                <Input
                    errorMessage="Informe um e-mail válido."
                    label="Email"
                    labelPlacement="inside"
                    name="email"
                    placeholder="Insira seu e-mail"
                    type="email"
                    value={email ? email : ''}
                    onChange={(e) => setEmail(e.target.value)}
                 />

                {!isHiddenComponent && (
                  <Input
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
                    onChange={(e) => setPassword(e.target.value)}
                  />
                )}


                {!isHiddenComponent && (
                  <Input
                    errorMessage={
                            submitted && !passwordsMatch ? "As senhas não coincidem" : ""
                    }
                    label="Confirme a Senha"
                    labelPlacement="inside"
                    name="confirm_password"
                    placeholder="Informe a senha novamente"
                    type={typeInputConfirmPassword}
                    minLength={8}
                    isInvalid={submitted && !passwordsMatch}
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
                    onChange={(e) => setConfirmPassword(e.target.value)}
                  />
                )}
                
                
                <Button color="primary" type="submit" className="w-full bg-green-500" disabled={disableSubmitButton}>
                    Salvar
                </Button>
                <Button variant="flat" className="w-full bg-red-500 text-white" hidden={!isHiddenComponent} onPress={hiddenInputPassword} disabled={disableSubmitButton}>
                    Mudar senha
                </Button>
                <Button variant="flat" className="w-full bg-blue-500 text-white" hidden={isHiddenComponent} onPress={hiddenInputPassword} disabled={disableSubmitButton}>
                    Manter senha
                </Button>
                
            </Form>
        </CardBody>
    </Card>
  )
}

export default EditForm