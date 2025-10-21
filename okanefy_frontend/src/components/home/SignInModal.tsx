import {
  Modal,
  ModalContent,
  ModalHeader,
  ModalBody,
  Button,
  Form,
  Input,
} from "@heroui/react";
import { useEffect, useState } from "react";
import { Link, useNavigate } from "react-router";
import { BASE_URL } from "../../utils/constants";
import { AiOutlineEye, AiOutlineEyeInvisible } from "react-icons/ai";

interface SignInModalProps {
  isOpen: boolean
  setIsOpen: React.Dispatch<React.SetStateAction<boolean>>
}


export default function SignInModal({ isOpen, setIsOpen }: SignInModalProps) {
  const navigate = useNavigate()
  const [registerError, setRegisterError] = useState('')
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [isVisiblePassword, setIsVisiblePassword] = useState(false)
  const [typeInputPassword, setTypeInputPassword] = useState("password")


  function changeInputPassword() {
    if(typeInputPassword == "password") {
      setIsVisiblePassword(true)
      setTypeInputPassword("text")
    } else {
      setIsVisiblePassword(false)
      setTypeInputPassword("password")
    }
  }

  function resetForm() {
    setEmail("");
    setPassword("");
    setRegisterError("");
  }
  
  async function signIn(e: any) {
    e.preventDefault();
    try {

      const requestBody = {
        "email": email,
        "password": password
      }

      const response = await fetch(BASE_URL + 'signIn', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(requestBody)
      })

      if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message || "Erro ao tentar realizar o login");
      }

      const data = await response.json()

      localStorage.setItem("id", data.id)
      localStorage.setItem("name", data.name)
      localStorage.setItem("email", data.email)
      localStorage.setItem("token", data.token)
      
      const savedToken = localStorage.getItem("token")
      if (savedToken) {
        navigate("/geral", { replace: true })
      }
    } catch (error: unknown) {
      if (error instanceof Error) {
        setRegisterError(error.message)
      } else {
        console.log(error)
      }
    }
  }

  useEffect(() => {
    if (!isOpen) {
      setEmail("")
      setPassword("")
    }
  }, [isOpen]);

  return (
    <>
      <Modal isOpen={isOpen} onOpenChange={setIsOpen} onSubmit={(e) => signIn(e)}>
        <ModalContent>
          {(onClose) => (
            <>
              <ModalHeader className="flex flex-col gap-1">
                <span className="text-center">Acesse sua conta</span>
                <span className="text-red-500 text-sm text-center">{registerError}</span>
              </ModalHeader>
              <ModalBody>
                <Form
                    className="w-full flex flex-col gap-4"
                    >
                    
                    <Input
                        isRequired
                        errorMessage="Informe um e-mail válido."
                        label="Email"
                        labelPlacement="inside"
                        name="email"
                        placeholder="Insira seu e-mail"
                        type="email"
                        onChange={(e) => setEmail(e.target.value)}
                        value={email}
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
                        onChange={(e) => setPassword(e.target.value)}
                        value={password}
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

                    <div className="w-full text-end">
                      <Link to="/esqueceu_senha" className="text-blue-400 font-semibold text-xs underline">
                        Esqueceu a senha?
                      </Link>
                    </div>
                    <div className="flex w-full gap-2">
                        <Button color="primary" type="submit" className="w-full bg-green-500">
                          Confirmar
                        </Button>
                        <Button type="reset" variant="flat" className="w-full bg-blue-500 text-white" onPress={resetForm}>
                          Limpar
                        </Button>
                    </div>
                </Form>
              </ModalBody>
            </>
          )}
        </ModalContent>
      </Modal>
    </>
  );
}
