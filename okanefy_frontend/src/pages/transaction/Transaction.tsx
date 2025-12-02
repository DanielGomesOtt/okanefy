import GeneralNavBar from "../../components/general/GeneralNavBar"
import TransactionMainContent from "../../components/transaction/TransactionMainContent"


function Transaction() {
  return (
    <div className='w-screen h-screen'>
        <div className='h-screen flex flex-col w-full'>
          <GeneralNavBar />
          <div className="flex-1 flex flex-col mt-24 items-center gap-4">
            <TransactionMainContent />
          </div>
        </div>
    </div>
  )
}

export default Transaction