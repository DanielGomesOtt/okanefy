import { Button, Card, CardBody, CardFooter, CardHeader, Divider, Form, getKeyValue, Input, Modal, ModalBody, ModalContent, Pagination, Select, SelectItem, Table, TableBody, TableCell, TableColumn, TableHeader, TableRow } from '@heroui/react'
import { useEffect, useState } from 'react';
import { FiArrowUp, FiArrowDown, FiX } from "react-icons/fi"
import { FiSearch, FiPlus, FiEdit2, FiTrash2 } from "react-icons/fi";
import { BASE_URL } from '../../utils/constants';

function PaymentMethodMainContent() {
    const [name, setName] = useState<string | null>(null)
    const [isInstallment, setIsInstallment] = useState<string>("all")
    const [paymentMethodVisible, setPaymentMethodVisible] = useState(false)
    const [totalPages, setTotalPages] = useState(1)
    const [currentPage, setCurrentPage] = useState(1)
    const [paymentMethods, setPaymentMethods] = useState<PaymentMethod[]>([])
    const [isOpenModal, setIsOpenModal] = useState(false)
    const [deletedPaymentMethodId, setDeletedPaymentMethodId] = useState<number | null>(null)
    const [editedPaymentMethodId, setEditedPaymentMethodId] = useState<number | null>(null)
    const [errorMessage, setErrorMessage] = useState("")

    const typesPayments = [
        {key: 'true', name: 'Parcelado'},
        {key: 'false', name: 'À vista'}
    ]

    interface PaymentMethod {
        id: number
        name: string
        isInstallment: string
    }

    async function getPaymentMethods(page: number = 1, nameParam: string | null, isInstallmentParam: string) {
        try {
            const params = new URLSearchParams({
                userId: String(localStorage.getItem('id')),
                page: String(page - 1),
                size: '25',
            })

            if (nameParam) params.append('name', nameParam)
            if (isInstallmentParam) params.append('isInstallment', isInstallmentParam)
            
            console.log(params.toString())

            const response = await fetch(`${BASE_URL}paymentMethod?${params}`, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${localStorage.getItem('token')}`
                }
            })

            
            if (response.status !== 200) {
                const errorData = await response.json()
                throw new Error(errorData.message || "Erro ao tentar consultar formas de pagamento.")
            }

            const data = await response.json()
            setTotalPages(data.totalPages)
            setCurrentPage(data.number + 1)
            if(data.paymentMethods.length > 0) {""
                setPaymentMethodVisible(true)
                setPaymentMethods(data.paymentMethods)
            } else {
                setPaymentMethods([])
                setPaymentMethodVisible(false)
            }

        } catch (error: unknown) {
            setPaymentMethods([])
            setPaymentMethodVisible(false)
            console.log(error)    
        }
    }

    async function deletePaymentMethod() {
        try {
            
            const response = await fetch(`${BASE_URL}paymentMethod/${deletedPaymentMethodId}`, {
                method: 'DELETE',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${localStorage.getItem('token')}`
                }
            })

            if (response.status !== 204) {
                const errorData = await response.json()
                throw new Error(errorData.message || "Erro ao tentar excluir a forma de pagamento.")
            }

            setIsOpenModal(false)
            getPaymentMethods(1, null, "all")

        } catch (error: unknown) {
            console.log(error)    
        }
    }

    async function savePaymentMethod() {
        try {
            const body = {
                "name": name,
                "isInstallment": isInstallment,
                "userId": localStorage.getItem("id")
            }

            const response = await fetch(`${BASE_URL}paymentMethod`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${localStorage.getItem('token')}`
                },
                body: JSON.stringify(body)
            })
            
            if(response.status !== 201) {
                const errorData = await response.json()
                throw new Error(errorData.message || "Erro ao tentar cadastrar uma nova forma de pagamento.")
            }
            
            setName(null)
            setIsInstallment('all')
            getPaymentMethods(currentPage, null, "all")
        } catch (error: unknown) {
            if (error instanceof Error) {
                setErrorMessage(error.message)
                setTimeout(() => {
                    setErrorMessage("")
                }, 5000)
            } else {
                console.log(error)
            }
        }
    }

    async function updatePaymentMethod() {
        try {
            const body = {
                "id": editedPaymentMethodId,
                "name": name,
                "isInstallment": isInstallment,
            }

            const response = await fetch(`${BASE_URL}paymentMethod`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${localStorage.getItem('token')}`
                },
                body: JSON.stringify(body)
            })
            
            if(response.status !== 200) {
                throw new Error("Erro ao tentar editar uma forma de pagamento.")
            }
            setEditedPaymentMethodId(null)
            setName(null)
            setIsInstallment("all")
            getPaymentMethods(currentPage, null, "all")
        } catch (error: unknown) {
            if (error instanceof Error) {
                setErrorMessage(error.message)
                setTimeout(() => {
                    setErrorMessage("")
                }, 5000)
            } else {
                console.log(error)
            }
        }
    }

    useEffect(() => {
        getPaymentMethods(1, null, "all")
    }, []);

    return (
        <div className='w-4/5 h-4/5'>
            <Card className='w-full h-full'>
                <CardHeader>
                    <Form className="w-full" onSubmit={(e) => e.preventDefault()}>
                        <div className='text-center w-full'>
                            <span className='text-center text-red-500'>{errorMessage}</span>
                        </div>
                        <div className="flex flex-wrap md:flex-nowrap items-center gap-4 w-full justify-center">
                            <Input
                                className="w-full md:w-96"
                                name="payment_method_input"
                                placeholder="Insira o nome de uma forma de pagamento."
                                type="text"
                                onChange={(e) => setName(e.target.value)}
                                value={name !== null ? name : ''}
                            />

                            <Select
                                label="Selecione o tipo de recebimento"
                                className="w-full md:max-w-xs"
                                 selectedKeys={
                                    isInstallment === "true"
                                        ? ["true"]
                                        : isInstallment === "false"
                                        ? ["false"]
                                        : ["all"]
                                }
                                onSelectionChange={(keys) => {
                                    const selected = Array.from(keys)[0] ?? null;

                                    if (selected === "true") {
                                        setIsInstallment("true");
                                    } else if (selected === "false") {
                                        setIsInstallment("false");
                                    } else {
                                        setIsInstallment("all");
                                    }
                                }}
                            >
                                <SelectItem key="all">Todos</SelectItem>

                                <>
                                    {typesPayments.map((t) => (
                                        <SelectItem key={t.key}>
                                        {t.name}
                                        </SelectItem>
                                    ))}
                                </>
                            </Select>

                            <div className="flex w-full md:w-auto justify-between md:justify-start gap-2">
                            {editedPaymentMethodId === null ? (
                                <>
                                    <Button
                                        className="bg-primary text-white flex-1 md:flex-none"
                                        startContent={<FiSearch size={18} />}
                                        onPress={() => getPaymentMethods(1, name, isInstallment)}
                                    >
                                        Pesquisar
                                    </Button>

                                    <Button
                                        className="bg-success text-white flex-1 md:flex-none"
                                        startContent={<FiPlus size={18} />}
                                        onPress={savePaymentMethod}
                                    >
                                        Adicionar
                                    </Button>
                                </>
                            ) : (
                                <>
                                    <Button
                                        className="bg-success text-white flex-1 md:flex-none"
                                        startContent={<FiEdit2 size={18} />}
                                        onPress={updatePaymentMethod}
                                    >
                                        Editar
                                    </Button>

                                    <Button
                                        className="bg-danger text-white flex-1 md:flex-none"
                                        startContent={<FiX size={18} />}
                                        onPress={() => {
                                            setEditedPaymentMethodId(null)
                                            setName("")
                                            setIsInstallment("all")
                                        }}
                                    >
                                        Cancelar
                                    </Button>
                                </>
                            )}
                            </div>
                        </div>    
                    </Form>
                </CardHeader>
                <Divider />
                <CardBody>
                    <Table aria-labelledby='Tabela de formaa de pagamento'>
                        <TableHeader>
                            <TableColumn key="id" className='text-bold text-blue-500 text-center'>ID</TableColumn>
                            <TableColumn key="name" className='text-bold text-blue-500 text-center'>Nome</TableColumn>
                            <TableColumn key="isInstallment" className='text-bold text-blue-500 text-center'>Tipo</TableColumn>
                            <TableColumn key="edit" className='text-bold text-blue-500 text-center'>Editar</TableColumn>
                            <TableColumn key="delete" className='text-bold text-blue-500 text-center'>Excluir</TableColumn>
                        </TableHeader>

                        <TableBody items={paymentMethods}>
                            {(paymentMethod) => (
                                <TableRow key={paymentMethod.id}>
                                {(columnKey) => (
                                    <TableCell className="text-center">
                                    {columnKey === "edit" ? (
                                        <Button
                                            isIconOnly
                                            size="sm"
                                            color="primary"
                                            variant="bordered"
                                            className="transition-all duration-200 hover:bg-primary hover:text-white"
                                            onPress={() => {
                                                setEditedPaymentMethodId(paymentMethod.id)
                                                setName(paymentMethod.name)
                                                setIsInstallment(paymentMethod.isInstallment)
                                            }}
                                        >
                                            <FiEdit2 className="w-4 h-4" />
                                        </Button>
                                    ) : columnKey === "delete" ? (
                                        <Button
                                            isIconOnly
                                            size="sm"
                                            color="danger"
                                            variant="bordered"
                                            className="transition-all duration-200 hover:bg-danger hover:variant-solid hover:text-white"
                                            onPress={() => {
                                                setIsOpenModal(true)
                                                setDeletedPaymentMethodId(paymentMethod.id)
                                            }}
                                        >
                                            <FiTrash2 className="w-4 h-4" />
                                        </Button>
                                    ) : columnKey === "isInstallment" ? (
                                        paymentMethod.isInstallment === "true" ? (
                                        <div className="flex items-center justify-center gap-1 text-blue-500 font-semibold">
                                            <span>Parcelado</span>
                                        </div>
                                        ) : (
                                        <div className="flex items-center justify-center gap-1 text-green-500 font-semibold">
                                            <span>À vista</span>
                                        </div>
                                        )
                                    ) : (
                                        getKeyValue(paymentMethod, columnKey)
                                    )}
                                    </TableCell>
                                )}
                                </TableRow>
                            )}
                        </TableBody>
                    </Table>
                </CardBody>
                <Divider />
                <CardFooter className='flex justify-center'>
                    <Pagination isCompact showControls initialPage={1} total={totalPages}  page={currentPage} size="sm" className="sm:size-md"
                    onChange={(page) => {
                        setCurrentPage(page);
                        getPaymentMethods(page, name, isInstallment);
                    }}/>
                </CardFooter>
            </Card>
            <Modal isOpen={isOpenModal} onOpenChange={setIsOpenModal} hideCloseButton>
                <ModalContent>
                    {() => (
                        <>
                            <ModalBody className="text-center">
                            <h3 className="text-xl font-bold">
                                Você tem certeza que deseja excluir essa forma de pagamento?
                            </h3>

                            <div className="flex justify-around">
                                <Button className="bg-green-500 text-white p-5" onPress={deletePaymentMethod}>Confirmar</Button>
                                <Button className="bg-red-500 text-white p-5" onPress={() => setIsOpenModal(false)}>Cancelar</Button>
                            </div>
                            </ModalBody>
                        </>
                    )}
                </ModalContent>
            </Modal>

        </div>
    )
}

export default PaymentMethodMainContent