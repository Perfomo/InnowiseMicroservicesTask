import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import MainPage from './mainPage/MainPage';
import MainHeader from './mainPage/MainHeader';
import LoginPage from './loginPage/LoginPage';
import RegisterPage from './RegisterPage/RegisterPage';
import CatalogPage from './CatalogPage/CatalogPage';
import ProfilePage from './ProfilePage/ProfilePage';
import ChangeUserInfoPage from './ChangeUserInfoPage/ChangeUserInfoPage';
import OrdersPage from './OrdersPage/OrdersPage';

const AppRouter: React.FC = () => {
  return (
    <Router>
      <Routes>
        <Route path='/' element={<MainPage />} />
        <Route path='/test' element={<MainHeader />} />
        <Route path='/login' element={<LoginPage />} />
        <Route path='/register' element={<RegisterPage />} />
        <Route path='/catalog' element={<CatalogPage />} />
        <Route path='/profile' element={<ProfilePage />} />
        <Route path='/changeUserInfo' element={<ChangeUserInfoPage />} />
        <Route path='/orders'element={<OrdersPage />} />
      </Routes>
    </Router>
  );
};

export default AppRouter;