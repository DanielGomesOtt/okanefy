import { Card, CardBody, CardHeader, Input } from '@heroui/react'
import { useEffect, useState } from 'react'
import { BASE_URL } from '../../utils/constants'


function GeneralMainContent() {

	const [initialDate, setInitialDate] = useState<string>("")
	const [endDate, setEndDate] = useState<string>("")
	const [movement, setMovement] = useState<string>("0,00")
	const [income, setIncome] = useState<string>("0,00")
	const [expense, setExpense] = useState<string>("0,00")

	async function getTransactions() {
        try {
            const params = new URLSearchParams({
                userId: String(localStorage.getItem('id')),
            })

			console.log(initialDate)
			console.log(endDate)

            if(initialDate !== "") params.append('initialDate', initialDate)
            if(endDate !== "") params.append('endDate', endDate)
                
            const response = await fetch(`${BASE_URL}transactions/general?${params}`, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${localStorage.getItem('token')}`
                }
            })

            if (response.status !== 200) {
                const errorData = await response.json()
                throw new Error(errorData.message || "Erro ao tentar consultar as transações.")
            }

            const data = await response.json()

			console.log(data)

			let sumExpense = 0
			let sumIncome = 0
			let sumMovement = 0

			data.forEach((transaction: any) => {
			const amount = Number(transaction.amount)

			if (transaction.category_type === 'EXPENSE') {
				sumExpense += amount
			}

			if (transaction.category_type === 'INCOME') {
				sumIncome += amount
			}

			sumMovement += amount
			})

			setExpense(sumExpense.toLocaleString('pt-BR', {style: 'currency', currency: 'BRL'}))
			setIncome(sumIncome.toLocaleString('pt-BR', {style: 'currency', currency: 'BRL'}))
			setMovement(sumMovement.toLocaleString('pt-BR', {style: 'currency', currency: 'BRL'}))
            
        } catch (error) {
            if(error instanceof Error) {
                console.log(error.message)
            }
        }
    }

	useEffect(() => {
		getTransactions()
	}, [initialDate, endDate])

	return (
		<div className='w-4/5 h-4/5'>
			<div className='w-full grid grid-cols-1 md:grid-cols-2 gap-4 mb-5'>
				<Input
					className="w-full"
					name="initial_date_input"
					type="date"
					label="Data inicial"
					onChange={(e) => {
						setInitialDate(e.target.value)
					}}
				/>

				<Input
					className="w-full"
					name="end_date_input"
					type="date"
					label="Data final"
					onChange={(e) => {
						setEndDate(e.target.value)
					}}
				/>
			</div>
			<div className='w-full h-full grid grid-cols-1 md:grid-cols-3 gap-4'>
				<Card className='w-full h-36 bg-blue-400'>
					<CardHeader className='text-center'>
						<div className='w-full text-center'>
							<span className='text-white text-semibold'>Movimentação</span>
						</div>
					</CardHeader>
					<CardBody>
						<div className='w-full text-center'>
							<span className='text-white text-semibold text-3xl'>{movement}</span>
						</div>
					</CardBody>
				</Card>

				<Card className='w-full h-36 bg-green-400'>
					<CardHeader>
						<div className='w-full text-center'>
							<span className='text-white text-semibold'>Entrada</span>
						</div>
					</CardHeader>
					<CardBody>
						<div className='w-full text-center'>
							<span className='text-white text-semibold text-3xl'>{income}</span>
						</div>
					</CardBody>
				</Card>

				<Card className='w-full h-36 bg-red-400'>
					<CardHeader>
						<div className='w-full text-center'>
							<span className='text-white text-semibold'>Saída</span>
						</div>
					</CardHeader>
					<CardBody>
						<div className='w-full text-center'>
							<span className='text-white text-semibold text-3xl'>{expense}</span>
						</div>
					</CardBody>
				</Card>
			</div>
		</div>
	)
}

export default GeneralMainContent