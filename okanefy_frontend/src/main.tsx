import { createRoot } from 'react-dom/client'
import './index.css'
import { BrowserRouter, Routes, Route } from 'react-router'
import { HeroUIProvider } from '@heroui/react';
import { StrictMode } from 'react';
import Home from './pages/home/Home';
import General from './pages/general/General';
import PublicRoute from './routes/PublicRoute';
import PrivateRoute from './routes/PrivateRoute';
import Profile from './pages/profile/Profile';
import ForgotPassword from './pages/forgot_password/ForgotPassword';
import Category from './pages/category/Category';

const rootElement = document.getElementById('root');

if (rootElement) {
  createRoot(rootElement).render(
    <StrictMode>
        <HeroUIProvider>
            <BrowserRouter>
                <Routes>
                    <Route path='/' element={<PublicRoute><Home /></PublicRoute>}/>
                    <Route path='/esqueceu_senha' element={<PublicRoute><ForgotPassword /></PublicRoute>}/>
                    <Route path='/geral' element={<PrivateRoute><General /></PrivateRoute>}/>
                    <Route path='/profile' element={<PrivateRoute><Profile /></PrivateRoute>}/>
                    <Route path='/categorias' element={<PrivateRoute><Category /></PrivateRoute>}/>
                </Routes>
            </BrowserRouter>
        </HeroUIProvider>
    </StrictMode>
  );
} else {
  console.error('Root element not found');
}
