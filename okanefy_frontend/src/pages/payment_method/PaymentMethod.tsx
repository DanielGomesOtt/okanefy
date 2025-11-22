import GeneralNavBar from "../../components/general/GeneralNavBar"
import PaymentMethodMainContent from "../../components/payment_method/PaymentMethodMainContent"

function PaymentMethod() {
  return (
    <div className='w-screen h-screen'>
        <div className='h-screen flex flex-col w-full'>
          <GeneralNavBar />
          <div className="flex-1 flex flex-col mt-24 items-center gap-4">
            <PaymentMethodMainContent />
          </div>
        </div>
    </div>
  )
}

export default PaymentMethod