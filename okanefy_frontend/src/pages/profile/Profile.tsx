import React, { useState } from 'react'
import GeneralNavBar from '../../components/general/GeneralNavBar'
import EditForm from '../../components/profile/EditForm'

function Profile() {
  const [userName, setUsername] = useState(localStorage.getItem('name'))
  return (
    <div className="h-screen flex flex-col">
      <GeneralNavBar />
      <div className="flex-1 flex justify-center items-center bg-gray-50">
        <EditForm />
      </div>
    </div>
  )
}

export default Profile