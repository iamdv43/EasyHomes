import React, { useState, lazy, Suspense, useEffect } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { Routes, Route, BrowserRouter as Router, Navigate, Outlet } from 'react-router-dom';

import "./App.css";
import NavBar from "./components/nav-bar/Navbar";
import HomeTabs from "./components/home-tabs/HomeTabs";
import FabMenu from "./components/fab-menu/FabMenu";
import Login from "./User/Login";
import Register from "./User/Register";
import Forgotpassword from "./User/Forgotpassword";
import Container from '@mui/material/Container';

import Grid from '@mui/material/Grid';
import Box from '@mui/material/Box';
import Collapse from '@mui/material/Collapse';



import Slide from '@mui/material/Slide';
import './App.css';
import { updateUserLoggedInStatus } from './reducers/app/appSlice';

import Welcome from './components/nav-bar/Welcome';
import Drawer from './components/nav-bar/drawer/Drawer';
import FilterMenu from './components/filter-menu/FilterMenu';
// const NavBar = lazy(() => import('./components/nav-bar/Navbar'));

import axios from 'axios';
import { IconButton } from '@mui/material';
import ArrowCircleRightOutlinedIcon from '@mui/icons-material/ArrowCircleRightOutlined';
import ArrowCircleLeftOutlinedIcon from '@mui/icons-material/ArrowCircleLeftOutlined';
import ChatRoom from './components/chat-room/ChatRoom';

import {over} from 'stompjs';
import SockJS from 'sockjs-client';
var stompClient = null;


const isLogin = () => {
  return !!localStorage.getItem("token");
  // return true;
}

const Public = () => <Login />;
const PublicRegister = () => <Register />;
const PublicForgotPassword = () => <Forgotpassword />;
const Private = () => <Dashboard />;
const PrivateChatRoom = () => <ChatRoomSection />;
// const Login = () => <div>login</div>;

function PrivateOutlet() {
  const auth = isLogin();
  return auth ? <Outlet /> : <Navigate to="/login" />;
}

function PrivateChatRoomOutlet() {
  const auth = useAuth();
  return auth ? <Outlet /> : <Navigate to="/login" />;
}

function PublicOutlet() {
  const auth = isLogin();
  return !auth ? <Outlet /> : <Navigate to="/dashboard" />;
}

function PublicChatRoomOutlet() {
  const auth = isLogin();
  return !auth ? <Outlet /> : <Navigate to="/chat-room" />;
}

function PublicRegisterOutlet() {
  const auth = isLogin();
  return !auth ? <Outlet /> : <Navigate to="/register" />;
}

function PrivateRoute({ children }) {
  const auth = useAuth();
  return auth ? children : <Navigate to="/login" />;
}

function useAuth() {
  return true;
}


function PublicForgotPasswordOutlet() {
  const auth = isLogin();
  return !auth ? <Outlet /> : <Navigate to="/forgotpassword" />;
}

function App() {

  const [userData, setUserData] = useState({
    username: '',
    receivername: '',
    connected: false,
    message: ''
  });

  // const connect = () => {
  //   let Sock = new SockJS('http://192.168.0.7:8080/ws');
  //   stompClient = over(Sock);
  //   stompClient.connect({},onConnected, onError);
  // }

  // const onConnected = () => {
  //   setUserData({...userData,"connected": true});
  //   stompClient.subscribe('/chatroom/public', onMessageReceived);
  //   // stompClient.subscribe('/user/'+userData.username+'/private', onPrivateMessage);
  //   userJoin();
  // }

  // const onMessageReceived = (payloadData) => {
  //   console.log(payloadData);
  // }

  // const onMessageReceived = (payload)=>{
  //   var payloadData = JSON.parse(payload.body);
  //   switch(payloadData.status){
  //     case "JOIN":
  //       if(!privateChats.get(payloadData.senderName)){
  //         privateChats.set(payloadData.senderName,[]);
  //         setPrivateChats(new Map(privateChats));
  //         if (payloadData.senderName != userData.username) {
  //             setTab(payloadData.senderName);
  //         }
  //       }
  //       userJoin();
  //       break;
  //     case "MESSAGE":
  //       publicChats.push(payloadData);
  //       setPublicChats([...publicChats]);
  //       break;
  //   }
  // }

  const userJoin=()=>{
    var chatMessage = {
      senderName: userData.username,
      status:"JOIN"
    };
    stompClient.send("/app/message", {}, JSON.stringify(chatMessage));
  }

  const onError = (err) => {
    console.log(err);
    
}

  return (
    <Routes>
      <Route path="/dashboard" element={<PrivateOutlet />}>
        <Route path="" element={<Private />} />
      </Route>
      <Route path="/chat-room" element={<PrivateChatRoomOutlet />}>
        <Route path="" element={<PrivateChatRoom />} />
      </Route>
      <Route path="login" element={<PublicOutlet />}>
        <Route path="" element={<Public />} />
      </Route>
        <Route path="register" element={<PublicRegisterOutlet />}>
              <Route path="" element={<PublicRegister />} />
       </Route>
         <Route path="forgotpassword" element={<PublicForgotPasswordOutlet />}>
               <Route path="" element={<PublicForgotPassword />} />
             </Route>

      <Route path="" element={<Navigate to="/login" />} />
      <Route path="*" element={<Navigate to="/login" />} />
    </Routes>

  );
}

const Dashboard = () => {
  const [checked, setChecked] = React.useState(true);
  // const [collapsibleCardWidth, setCollapsibleCardWidth] = React.useState(25);

  const getCollapsibleCardWidth = () => {
    if (checked) {
      return 25 + '%';
    } else {
      return 5 + '%';
    }
  };

  const getContentWidth = () => {
    if (checked) {
      return 75 + '%';
    } else {
      return 95 + '%';
    }
  };

  const filterMenuBar = (
    <FilterMenu 
      setChecked={setChecked}
      checked={checked}
      />
    // </FilterMenu>
  );
  return (
    <>
      <NavBar />
      {/* <HomeTabs />
      <FabMenu /> */}
      <Box sx={{ width: '100%', height: '100%',
        display: 'flex',
        flexDirection: 'row',
        flexWrap: 'no-wrap',
        justifyContent: 'space-around' }}>
        <Box sx={{ width: getCollapsibleCardWidth(),
          '& .MuiCollapse-wrapperInner': {
            width: '100%',
          }
        }}>
          <Collapse orientation="horizontal" in={checked} collapsedSize={50}>
            {filterMenuBar}
          </Collapse>
        </Box>
        <Box sx={{ width: getContentWidth() }}>
          <Box>
            <HomeTabs />
            <FabMenu />
          </Box>
        </Box>
      </Box>
      {/* <ChatRoom></ChatRoom> */}
      {/* <ChatRoomSection></ChatRoomSection> */}
      {/* <Container>
        <FilterMenu />
      </Container>
      <Box>
          <HomeTabs />
          <FabMenu />
      </Box> */}
    </>
  );
};

const ChatRoomSection = () => {
  return (
    <>
      <NavBar />
      <ChatRoom></ChatRoom>
    </>
  );
}


export default App;
