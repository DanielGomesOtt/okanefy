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

interface TransactionPaymentMethod {
    id: number
    payment_method_id: number
}

interface TransactionEdit {
    id: number,
    description: string,
    initial_date: string,
    frequency: string,
    category_id: number,
    installments: number,
    amount: string,
    payment_methods: TransactionPaymentMethod[]
}

interface UpdateTransactionFormProps {
  isOpen: boolean
  setIsOpen: React.Dispatch<React.SetStateAction<boolean>>
  parentPaymentMethods: PaymentMethod[]
  parentCategories: Category[]
  getTransactions: Function
  currentPage: number
  updatedTransaction: number
  setUpdatedTransaction: React.Dispatch<React.SetStateAction<number>>
  transactionEditData: TransactionEdit | null
}

interface PostPaymentMethod {
    transactionPaymentId: number
    paymentMethodId: number
    name: string
}




function UpdateTransactionForm({ isOpen, setIsOpen, parentPaymentMethods, parentCategories, getTransactions, currentPage, updatedTransaction, setUpdatedTransaction, transactionEditData }: UpdateTransactionFormProps) {

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
    const [postPaymentMethod, setPostPaymentMethod] = useState<PostPaymentMethod[]>([])
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
        setDescription("")
        setAmount("")
        setInitialDate("")
        setInstallments(1)
    }

    async function removePostPaymentMethod(
        transactionPaymentId: number,
        paymentMethodId: number
    ) {
        try {
            if (postPaymentMethod.length === 1) {
                setErrorMessage("A transação não pode ficar sem formas de pagamento.")
                return
            }

            setPostPaymentMethod(prev =>
                prev.filter(pm => pm.paymentMethodId !== paymentMethodId)
            )

            const response = await fetch(
                `${BASE_URL}transactionsPaymentMethod/${transactionPaymentId}`,
                {
                    method: 'DELETE',
                    headers: {
                        'Content-Type': 'application/json',
                        'Authorization': `Bearer ${localStorage.getItem('token')}`
                    }
                }
            )

            if (response.status !== 204) {
                const errorData = await response.json()
                throw new Error(errorData.message)
            }

            setErrorMessage("")
            getTransactions(currentPage)
        } catch (error) {
            if (error instanceof Error) setErrorMessage(error.message)
        }
    }


    async function addPostPaymentMethod(transactionId: number) {
        try {
            if (!paymentMethod) return

            const paymentMethodId = Number(paymentMethod)

            const alreadyExists = postPaymentMethod.some(
                ppm => ppm.paymentMethodId === paymentMethodId
            )

            if (alreadyExists) {
                setErrorMessage("Essa forma de pagamento já foi adicionada.")
                return
            }

            const response = await fetch(`${BASE_URL}transactionsPaymentMethod`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${localStorage.getItem('token')}`
                },
                body: JSON.stringify({
                    transaction_id: transactionId,
                    payment_method_id: paymentMethodId
                })
            })

            if (response.status !== 201) {
                const errorData = await response.json()
                throw new Error(
                    errorData.message ||
                    "Erro ao tentar adicionar uma forma de pagamento para a transação."
                )
            }

            
            const created: { id: number; payment_method_id: number } =
                await response.json()

            const found = paymentMethods.find(
                pm => pm.id === created.payment_method_id
            )

            if (!found) return

            setPostPaymentMethod(prev => [
                ...prev,
                {
                    transactionPaymentId: created.id,
                    paymentMethodId: created.payment_method_id,
                    name: found.name
                }
            ])

            setErrorMessage("")
            getTransactions(currentPage)
        } catch (error: unknown) {
            if (error instanceof Error) {
                setErrorMessage(error.message)
            } else {
                console.error(error)
            }
        }
    }


    function validateUpdateForm() {
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

    async function updateTransaction(e: React.FormEvent) {
        e.preventDefault()

        try {
            if (!validateUpdateForm()) return

            const body = {
                id: updatedTransaction,
                initial_date: initialDate,
                amount: Number(amount.replaceAll(",", ".")),
                description,
                number_installments: installments,
                category_id: Number(category),
                frequency
            }

            const response = await fetch(`${BASE_URL}transactions`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${localStorage.getItem('token')}`
                },
                body: JSON.stringify(body)
            })

            if (response.status !== 200) {
                const errorData = await response.json()
                throw new Error(
                    errorData.message ||
                    "Erro ao tentar atualizar a transação."
                )
            }

            setIsOpen(false)
            resetForm()
            getTransactions(currentPage)
        } catch (error: unknown) {
            if (error instanceof Error) {
                setErrorMessage(error.message)
            } else {
                console.error(error)
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

    useEffect(() => {
        if (!transactionEditData) return

        setDescription(transactionEditData.description)
        setInitialDate(transactionEditData.initial_date)
        setFrequency(transactionEditData.frequency)
        setCategory(transactionEditData.category_id.toString())
        setInstallments(transactionEditData.installments)
        setAmount(
            Number(transactionEditData.amount)
                .toFixed(2)
                .replace(".", ",")
        )

        const mapped = transactionEditData.payment_methods.map(pm => {
            const found = parentPaymentMethods.find(
                p => p.id === pm.payment_method_id
            )

            return {
                transactionPaymentId: pm.id,
                paymentMethodId: pm.payment_method_id,
                name: found?.name || ''
            }
        })

        setPostPaymentMethod(mapped)

        if (mapped.length > 0) {
            setPaymentMethod(mapped[0].paymentMethodId.toString())
        }
    }, [transactionEditData, parentPaymentMethods])



    
    return (
        <>
        <Modal isOpen={isOpen} onOpenChange={(open) => {
                setIsOpen(open)
                if(!open) {
                    resetForm()
                }
            }} 
            isDismissable={false}
            onSubmit={(e) => updateTransaction(e)}
            >
            <ModalContent>
            {(onClose) => (
                <>
                    <ModalHeader className="flex flex-col gap-1">
                        <span className="text-center">Editar transação</span>
                        <span className="text-red-500 text-sm text-center">{errorMessage}</span>
                    </ModalHeader>
                    <ModalBody>
                        <Form
                            className="w-full flex flex-col gap-4"
                            onSubmit={(e) => updateTransaction(e)}
                            >
                            
                            <Input
                                isRequired
                                errorMessage="Informe uma descrição."
                                label="Descrição"
                                labelPlacement="inside"
                                name="description_create_modal_input"
                                placeholder="Insira uma descrição"
                                value={description}
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
                                    value={initialDate}
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
                                value={installments.toString()}
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
                                value={amount}
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
                                    onPress={() => addPostPaymentMethod(updatedTransaction)}
                                >
                                    <FiPlus size={18} />
                                </Button>  
                            </div>

                            <div className='flex flex-wrap gap-2'>
                                {postPaymentMethod.map((ppm) => (
                                    <Chip
                                        key={ppm.transactionPaymentId}
                                        onClose={() =>
                                            removePostPaymentMethod(
                                                ppm.transactionPaymentId,
                                                ppm.paymentMethodId
                                            )
                                        }
                                    >
                                        {ppm.name}
                                    </Chip>
                                ))}
                            </div>


                            <div className="flex w-full gap-2">
                                <Button color="primary" type="submit" className="w-full bg-green-500">
                                    Confirmar
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

export default UpdateTransactionForm