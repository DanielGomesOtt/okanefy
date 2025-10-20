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
import { useNavigate } from "react-router";
import { BASE_URL } from "../../utils/constants";

interface SignUpModalProps {
  isOpen: boolean;
  setIsOpen: React.Dispatch<React.SetStateAction<boolean>>;
}



export default function SignUpModal({ isOpen, setIsOpen }: SignUpModalProps) {
  const navigate = useNavigate();
  const [registerError, setRegisterError] = useState('');
  const [email, setEmail] = useState('');
  const [name, setName] = useState('');
  const [password, setPassword] = useState('');

  function resetForm() {
    setName("");
    setEmail("");
    setPassword("");
    setRegisterError("");
  }
  
  async function signUp(e: any) {
    e.preventDefault();
    try {

      const requestBody = {
        "name": name,
        "email": email,
        "password": password
      }

      const response = await fetch(BASE_URL + 'signUp', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(requestBody)
      })

      if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message || "Erro ao cadastrar usuário");
      }

      const data = await response.json()

      localStorage.setItem("id", data.id)
      localStorage.setItem("name", data.name)
      localStorage.setItem("email", data.email)
      localStorage.setItem("token", data.token)

      navigate("/geral", {replace: true})
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
      setName("")
      setEmail("")
      setPassword("")
    }
  }, [isOpen]);

  return (
    <>
      <Modal isOpen={isOpen} onOpenChange={setIsOpen} onSubmit={(e) => signUp(e)}>
        <ModalContent>
          {(onClose) => (
            <>
              <ModalHeader className="flex flex-col gap-1">
                <span className="text-center">Cadastre-se</span>
                <span className="text-red-500 text-sm text-center">{registerError}</span>
              </ModalHeader>
              <ModalBody>
                <Form
                    className="w-full flex flex-col gap-4"
                    >
                    <Input
                        isRequired
                        errorMessage="Informe um nome."
                        label="Nome"
                        labelPlacement="inside"
                        name="name"
                        placeholder="Insira seu nome"
                        type="text"
                        minLength={2}
                        onChange={(e) => setName(e.target.value)}
                        value={name}
                    />

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
                        type="password"
                        minLength={8}
                        onChange={(e) => setPassword(e.target.value)}
                        value={password}
                    />
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
