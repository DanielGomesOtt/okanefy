import { Button, Chip, Form, Input, Modal, ModalBody, ModalContent, ModalHeader, Select, SelectItem } from '@heroui/react'
import React, { useEffect, useState } from 'react'
import { FiPlus } from 'react-icons/fi'
import { BASE_URL } from '../../utils/constants'


interface PaymentMethod {
    id: number
    name: string
    isInstallment: string
}

interface Category {
    id: number
    name: string
    type: string
}

interface CreateTransactionFormProps {
  isOpen: boolean
  setIsOpen: React.Dispatch<React.SetStateAction<boolean>>
  parentPaymentMethods: PaymentMethod[]
  parentCategories: Category[]
  getTransactions: Function
  currentPage: number
}

function CreateTransactionForm({ isOpen, setIsOpen, parentPaymentMethods, parentCategories, getTransactions, currentPage }: CreateTransactionFormProps) {

    const typeFrequency = [
        {key: 'none', name: 'Não frequente'},
        {key: 'daily', name: 'Diario'},
        {key: 'monthly', name: 'Mensal'},
        {key: 'yearly', name: 'Anual'}
    ]

    const [errorMessage, setErrorMessage] = useState('')
    const [frequency, setFrequency] = useState('none')
    const [paymentMethods, setPaymentMethods] = useState(parentPaymentMethods)
    const [categories, setCategories] = useState(parentCategories)
    const [paymentMethod, setPaymentMethod] = useState<string>("")
    const [category, setCategory] = useState<string>("")
    const [postPaymentMethod, setPostPaymentMethod] = useState<PaymentMethod[]>([])
    const [description, setDescription] = useState("")
    const [initialDate, setInitialDate] = useState("")
    const [installments, setInstallments] = useState(1)
    const [amount, setAmount] = useState("")


    

    function resetForm () {
        setErrorMessage("")
        setFrequency("none")
        setPaymentMethod(parentPaymentMethods[0].id.toString())
        setCategory(parentCategories[0].id.toString())
        setPostPaymentMethod([])
    }

    function removePostPaymentMethod (paymentMethodId: number) {
        setPostPaymentMethod(prev =>
            prev.filter(pm => pm.id !== paymentMethodId)
        );
    }

    function addPostPaymentMethod() {
        if (!paymentMethod) return;

        const alreadyExists = postPaymentMethod.some(
            (ppm) => ppm.id.toString() === paymentMethod
        );

        if (alreadyExists) {
            setErrorMessage("Essa forma de pagamento já foi adicionada.");
            return;
        }

        const found = paymentMethods.find(pm => pm.id.toString() === paymentMethod);

        if (found) {
            setPostPaymentMethod(prev => [...prev, found]);
            setErrorMessage("");
        }
    }

    function validateCreateForm() {
        if(!description || description.length == 0) {
            setErrorMessage("Informe a descrição")
            return false
        }

        if(!initialDate || initialDate.length == 0) {
            setErrorMessage("Informe a data inicial")
            return false
        }

        if(!installments || !Number.isInteger(installments) || installments <= 0) {
            setErrorMessage("Informe o número de parcelas.")
            return false
        }

        if(!amount || Number.isNaN(Number(amount.replaceAll(",", ".")))) {
            setErrorMessage("Informe um valor válido para a transação.")
            return false
        }

        if(postPaymentMethod.length == 0) {
            setErrorMessage("Informe pelo menos uma forma de pagamento utilizada na transação.")
            return false
        }

        if(!category || category.length == 0) {
            setErrorMessage("Informe uma categoria.")
            return false
        }

        return true
    }

    async function createTransaction(e: any) {
        e.preventDefault()
        try {
            if(!validateCreateForm()) {
                return
            }

            let body = {
                initial_date: initialDate,
                amount: Number(amount.replaceAll(",", ".")),
                description: description,
                number_installments: installments,
                user_id: localStorage.getItem("id"),
                category_id: category,
                payment_methods: postPaymentMethod.map(pm => pm.id),
                frequency: frequency
            }

            const response = await fetch(`${BASE_URL}transactions`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${localStorage.getItem('token')}`
                },
                body: JSON.stringify(body)
            })

            if(response.status !== 201) {
                const errorData = await response.json()
                throw new Error(errorData.message || "Erro ao tentar criar a transação.")
            }

            setIsOpen(false)
            resetForm()
            getTransactions(currentPage)
        } catch (error: unknown){
            if (error instanceof Error) {
                setErrorMessage(error.message)
            } else {
                console.log(error)
            }
        }
    }

     
    useEffect(() => {
        if (parentPaymentMethods.length > 0) {
            const first = parentPaymentMethods[0].id.toString();
            setPaymentMethod(first);
            setPaymentMethods(parentPaymentMethods);
        }
    }, [parentPaymentMethods]);

    useEffect(() => {
        if (parentCategories.length > 0) {
            const first = parentCategories[0].id.toString();
            setCategory(first);
            setCategories(parentCategories);
        }
    }, [parentCategories])

    
    return (
        <>
        <Modal isOpen={isOpen} onOpenChange={(open) => {
                setIsOpen(open)
                if(!open) {
                    resetForm()
                }
            }} isDismissable={false}>
            <ModalContent>
            {(onClose) => (
                <>
                    <ModalHeader className="flex flex-col gap-1">
                        <span className="text-center">Adicione uma transação</span>
                        <span className="text-red-500 text-sm text-center">{errorMessage}</span>
                    </ModalHeader>
                    <ModalBody>
                        <Form
                            className="w-full flex flex-col gap-4"
                            onSubmit={(e) => createTransaction(e)}
                            >
                            
                            <Input
                                isRequired
                                errorMessage="Informe uma descrição."
                                label="Descrição"
                                labelPlacement="inside"
                                name="description_create_modal_input"
                                placeholder="Insira uma descrição"
                                type="text"
                                onChange={(e) => setDescription(e.target.value)}
                            />

                            <div className='grid grid-cols-1 lg:grid-cols-2 gap-4 w-full'>
                                <Input
                                    isRequired
                                    errorMessage="Informe a data inicial."
                                    label="Data Inicial"
                                    labelPlacement="inside"
                                    name="initial_date_create_modal_input"
                                    placeholder="Insira a data inicial"
                                    type="date"
                                    className='w-full'
                                    onChange={(e) => setInitialDate(e.target.value)}
                                />

                                <Select
                                    isRequired
                                    errorMessage="Informe a frequência da transação."
                                    label="Frequência"
                                    labelPlacement="inside"
                                    name="frequency_create_modal_input"
                                    placeholder="Informe a frequência da transação."
                                    selectedKeys={[frequency]}
                                    className='w-full'
                                    onSelectionChange={(keys) => {
                                        const key = Array.from(keys)[0];
                                        setFrequency(key.toString());
                                    }}
                                >
                                    <>
                                        {
                                            typeFrequency.map((type) => (
                                                <SelectItem key={type.key}>{type.name}</SelectItem>
                                            ))
                                        }
                                    </>

                                </Select>
                            </div>

                            <Select
                                isRequired
                                errorMessage="Informe uma categoria."
                                label="Categoria"
                                labelPlacement="inside"
                                name="category_create_modal_input"
                                placeholder="Informe uma categoria."
                                selectedKeys={category ? [category] : []}
                                className='w-full'
                                onSelectionChange={(keys) => {
                                    const key = Array.from(keys)[0];
                                    setCategory(key.toString());
                                }}
                            >
                                <>
                                    {
                                        categories.map((c) => (
                                            <SelectItem key={c.id.toString()}>{c.name}</SelectItem>
                                        ))
                                    }
                                </>

                            </Select>


                            
                            <Input
                                isRequired
                                errorMessage="Informe o número de parcelas."
                                label="Parcelas"
                                labelPlacement="inside"
                                name="number_installments_create_modal_input"
                                placeholder="Informe o número de parcelas"
                                type="number"
                                className='w-full'
                                min={1}
                                onChange={(e) => setInstallments(parseInt(e.target.value))}
                            />
                        
                            <Input
                                isRequired
                                errorMessage="Informe o valor da transação."
                                label="Valor (R$)"
                                labelPlacement="inside"
                                name="amount_create_modal_input"
                                placeholder="Informe o valor da transação"
                                type="text"
                                className='w-full'
                                onChange={(e) => setAmount(e.target.value)}
                            />

                            <div className='flex w-full items-center gap-2'>
                                <Select
                                    isRequired
                                    errorMessage="Informe uma ou mais formas de pagamento."
                                    label="Forma(s) de pagamento"
                                    labelPlacement="inside"
                                    name="payment_method_create_modal_input"
                                    placeholder="Informe uma ou mais formas de pagamento."
                                    selectedKeys={paymentMethod ? [paymentMethod] : []}
                                    className='w-3/4'
                                    onSelectionChange={(keys) => {
                                        const key = Array.from(keys)[0];
                                        setPaymentMethod(key.toString());
                                    }}
                                >
                                    <>
                                        {
                                            paymentMethods.map((p) => (
                                                <SelectItem key={p.id.toString()}>{p.name}</SelectItem>
                                            ))
                                        }
                                    </>

                                </Select>

                                <Button
                                    className="bg-success text-white w-1/4"
                                    onPress={addPostPaymentMethod}
                                >
                                    <FiPlus size={18} />
                                </Button>  
                            </div>

                            <div className='flex flex-wrap gap-2'>
                                {postPaymentMethod.map((ppm) => (
                                    <Chip onClose={() => removePostPaymentMethod(ppm.id)}>{ppm.name}</Chip>
                                ))}
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
    )
}

export default CreateTransactionForm