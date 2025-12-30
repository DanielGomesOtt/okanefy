import { Card, CardBody, CardHeader, Input } from '@heroui/react'
import { useEffect, useRef, useState } from 'react'
import { BASE_URL } from '../../utils/constants'
import { Chart } from 'chart.js/auto'


interface ChartTransaction {
	date: string
	amount: number
}

function GeneralMainContent() {

	const [initialDate, setInitialDate] = useState<string>("")
	const [endDate, setEndDate] = useState<string>("")
	const [movement, setMovement] = useState<string>("0,00")
	const [income, setIncome] = useState<string>("0,00")
	const [expense, setExpense] = useState<string>("0,00")
	const [floatIncome, setFloatIncome] = useState<number>(0.00)
	const [floatExpense, setFloatExpense] = useState<number>(0.00)
	const [floatMovement, setFloatMovement] = useState<number>(0.00)
	const [chartTransactions, setChartTransactions] = useState<ChartTransaction[]>([])
	const [chartIncomes, setChartIncomes] = useState<ChartTransaction[]>([])
	const [chartExpenses, setChartExpenses] = useState<ChartTransaction[]>([])
	const chartRef = useRef<Chart | null>(null)
	const canvasRef = useRef<HTMLCanvasElement | null>(null)
	const chartExpensesRef = useRef<Chart | null>(null)
	const canvasExpensesRef = useRef<HTMLCanvasElement | null>(null)
	const chartIncomesRef = useRef<Chart | null>(null)
	const canvasIncomesRef = useRef<HTMLCanvasElement | null>(null)

	

	async function getTransactions() {
        try {
            const params = new URLSearchParams({
                userId: String(localStorage.getItem('id')),
            })

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

			let sumExpense = 0.00
			let sumIncome = 0.00
			let sumMovement = 0.00

			let chartDataArray: ((prevState: ChartTransaction[]) => ChartTransaction[]) | { date: any; amount: any }[] = []
			let chartIncomesDataArray: ((prevState: ChartTransaction[]) => ChartTransaction[]) | { date: any; amount: any }[] = []
			let chartExpensesDataArray: ((prevState: ChartTransaction[]) => ChartTransaction[]) | { date: any; amount: any }[] = []

			data.forEach((transaction: any) => {
				const amount = Number(transaction.amount)
				
				let chartData = {
					'date': transaction.initial_date,
					'amount': transaction.amount
				}

				if (transaction.category_type === 'EXPENSE') {
					sumExpense += amount
					chartExpensesDataArray.push(chartData)
				}

				if (transaction.category_type === 'INCOME') {
					sumIncome += amount
					chartIncomesDataArray.push(chartData)
				}

				sumMovement += amount

				chartDataArray.push(chartData)
			})

			setExpense(sumExpense.toLocaleString('pt-BR', {style: 'currency', currency: 'BRL'}))
			setIncome(sumIncome.toLocaleString('pt-BR', {style: 'currency', currency: 'BRL'}))
			setMovement(sumMovement.toLocaleString('pt-BR', {style: 'currency', currency: 'BRL'}))
			setFloatExpense(sumExpense)
			setFloatIncome(sumIncome)
			setFloatMovement(sumMovement)
			setChartTransactions(chartDataArray)
			setChartIncomes(chartIncomesDataArray)
			setChartExpenses(chartExpensesDataArray)
            
        } catch (error) {
            if(error instanceof Error) {
                console.log(error.message)
            }
        }
    }

	function mountTransactionChart() {
		if (!canvasRef.current) return

		const labels = chartTransactions.map(t => t.date)
		const values = chartTransactions.map(t => t.amount)

		if (chartRef.current) {
			chartRef.current.destroy()
		}

		chartRef.current = new Chart(canvasRef.current, {
			type: 'line',
			data: {
				labels,
				datasets: [
					{
						label: 'Movimentações',
						data: values,
						backgroundColor: '#3b82f6'
					}
				]
			},
			options: {
				responsive: true,
				maintainAspectRatio: false,
				scales: {
					x: {
						display: false
					},
					y: {
						beginAtZero: true
					}
				}
			}
		})
	}

	function mountExpensesChart() {
		if (!canvasExpensesRef.current) return

		const labels = chartExpenses.map(t => t.date)
		const values = chartExpenses.map(t => t.amount)

		if (chartExpensesRef.current) {
			chartExpensesRef.current.destroy()
		}

		chartExpensesRef.current = new Chart(canvasExpensesRef.current, {
			type: 'line',
			data: {
				labels,
				datasets: [
					{
						label: 'Gastos',
						data: values,
						backgroundColor: '#ef4444'
					}
				]
			},
			options: {
				responsive: true,
				maintainAspectRatio: false,
				scales: {
					x: {
						display: false
					},
					y: {
						beginAtZero: true
					}
				}
			}
		})
	}

	function mountIncomesChart() {
		if (!canvasIncomesRef.current) return

		const labels = chartIncomes.map(t => t.date)
		const values = chartIncomes.map(t => t.amount)

		if (chartIncomesRef.current) {
			chartIncomesRef.current.destroy()
		}

		chartIncomesRef.current = new Chart(canvasIncomesRef.current, {
			type: 'line',
			data: {
				labels,
				datasets: [
					{
						label: 'Renda',
						data: values,
						backgroundColor: '#22c55e'
					}
				]
			},
			options: {
				responsive: true,
				maintainAspectRatio: false,
				scales: {
					x: {
						display: false
					},
					y: {
						beginAtZero: true
					}
				}
			}
		})
	}

	useEffect(() => {
		mountTransactionChart()
		mountExpensesChart()
		mountIncomesChart()

		return () => {
			if (chartRef.current) {
				chartRef.current.destroy()
				chartRef.current = null
			}

			if (chartExpensesRef.current) {
				chartExpensesRef.current.destroy()
				chartExpensesRef.current = null
			}

			if (chartIncomesRef.current) {
				chartIncomesRef.current.destroy()
				chartIncomesRef.current = null
			}
		}
	}, [chartTransactions, chartExpenses, chartIncomes])


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
			<div className='w-full mb-24 grid grid-cols-1 md:grid-cols-3 gap-4'>
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
			<div className="w-full grid grid-cols-1 md:grid-cols-3 gap-4">
				<Card className="p-4 h-72">
					<canvas ref={canvasRef} className="w-full h-full" />
				</Card>

				<Card className="p-4 h-72">
					<canvas ref={canvasIncomesRef} className="w-full h-full" />
				</Card>

				<Card className="p-4 h-72">
					<canvas ref={canvasExpensesRef} className="w-full h-full" />
				</Card>
			</div>

		</div>
	)
}

export default GeneralMainContent