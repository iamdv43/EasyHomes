import * as React from "react";
import PropTypes from "prop-types";

import CssBaseline from "@mui/material/CssBaseline";
import { ThemeProvider } from "@mui/material/styles";
import StarOutlineIcon from '@mui/icons-material/StarOutline';
import { styled, alpha } from "@mui/material/styles";
import AppBar from "@mui/material/AppBar";
import Box from "@mui/material/Box";
import Toolbar from "@mui/material/Toolbar";
import IconButton from "@mui/material/IconButton";
import Typography from "@mui/material/Typography";
import InputBase from "@mui/material/InputBase";
import Badge from "@mui/material/Badge";
import MenuItem from '@mui/material/MenuItem';
import Menu from '@mui/material/Menu';
import SearchIcon from "@mui/icons-material/Search";
import AccountCircle from "@mui/icons-material/AccountCircle";
import MailIcon from "@mui/icons-material/Mail";
import MoreIcon from "@mui/icons-material/MoreVert";
import ChatIcon from '@mui/icons-material/Chat';

import { customTheme } from '../../utils/theme';
import { logoutUser } from '../../reducers/app/appSlice';

import { useDispatch, useSelector } from "react-redux";
import { useNavigate } from 'react-router-dom';

import { filterProperties, filterServices } from '../../reducers/app/thunks/appThunk';

import {useState} from "react";
import FavList from "../favoritesList/FavList"
import {Modal} from "@mui/material";

function ElevationScroll(props) {
  const { children } = props;

  return React.cloneElement(children);
}

ElevationScroll.propTypes = {
  children: PropTypes.element.isRequired,
};

const Search = styled("div")(({ theme }) => ({
  position: "relative",
  borderRadius: theme.shape.borderRadius,
  backgroundColor: alpha(theme.palette.common.white, 0.15),
  "&:hover": {
    backgroundColor: alpha(theme.palette.common.white, 0.25),
  },
  marginRight: theme.spacing(2),
  marginLeft: 0,
  width: "100%",
  [theme.breakpoints.up("sm")]: {
    marginLeft: theme.spacing(3),
    width: "auto",
  },
}));

const SearchIconWrapper = styled("div")(({ theme }) => ({
  padding: theme.spacing(0, 2),
  height: "100%",
  position: "absolute",
  pointerEvents: "none",
  display: "flex",
  alignItems: "center",
  justifyContent: "center",
}));

const StyledInputBase = styled(InputBase)(({ theme }) => ({
  color: "inherit",
  "& .MuiInputBase-input": {
    padding: theme.spacing(1, 1, 1, 0),
    // vertical padding + font size from searchIcon
    paddingLeft: `calc(1em + ${theme.spacing(4)})`,
    transition: theme.transitions.create("width"),
    width: "100%",
    [theme.breakpoints.up("md")]: {
      width: "20ch",
      '&:focus': {
        width: '30ch',
      },
    },
  },
}));

export default function ElevateAppBar(props) {
  const title = 'Easy Homes'
  const navBarTheme = customTheme;

  const [anchorEl, setAnchorEl] = React.useState(null);
  const [filterAnchorEl, setFilterAnchorEl] = React.useState(null);
  const [mobileMoreAnchorEl, setMobileMoreAnchorEl] = React.useState(null);
  const [favouritesCount, setFavouritesCount] = React.useState(0); // eslint-disable-line
  const [messagesCount, setMessagesCount] = React.useState(0); // eslint-disable-line

  const dispatch = useDispatch();
  const navigate = useNavigate();

  const currentTab = useSelector(state => state.app.currentTab);
  // setFavouritesCount(0);
  // setMessagesCount(0);

  const isMenuOpen = Boolean(anchorEl);
  const isMobileMenuOpen = Boolean(mobileMoreAnchorEl);

  // const isFilterMenuOpen = Boolean(anchorEl);

  const handleProfileMenuOpen = (event) => {
    setAnchorEl(event.currentTarget);
  };

  // const handleFilterMenuOpen = (event) => {
  //   setAnchorEl(event.currentTarget);
  // };

  const handleMobileMenuClose = () => {
    setMobileMoreAnchorEl(null);
  };

  const handleMenuClose = () => {
    setAnchorEl(null);
    handleMobileMenuClose();
  };

  // const handleFilterMenuClose = () => {
  //   setFilterAnchorEl(null);
  //   handleMobileMenuClose();
  // };

  const handleLogout = () => {
    dispatch(logoutUser({ isUserLoggedIn: false }));
    setAnchorEl(null);
    handleMobileMenuClose();
    
    navigate('/login');
  };

  const handleMobileMenuOpen = (event) => {
    setMobileMoreAnchorEl(event.currentTarget);
  };

  const navigateToChatRoom = () => {
    setAnchorEl(null);
    handleMobileMenuClose();
    navigate('/chat-room');
  };

  const menuId = "primary-search-account-menu";
  // const filterMenuId = "primary-search-account-menu";
  const renderMenu = (
    <Menu
      anchorEl={anchorEl}
      anchorOrigin={{
        vertical: "top",
        horizontal: "right",
      }}
      id={menuId}
      keepMounted
      transformOrigin={{
        vertical: "top",
        horizontal: "right",
      }}
      open={isMenuOpen}
      onClose={handleMenuClose}
    >
      {/* <MenuItem onClick={handleMenuClose}>Profile</MenuItem> */}
      <MenuItem onClick={handleLogout}>Logout</MenuItem>
    </Menu>
  );

  const mobileMenuId = "primary-search-account-menu-mobile";
  const renderMobileMenu = (
    <Menu
      anchorEl={mobileMoreAnchorEl}
      anchorOrigin={{
        vertical: "top",
        horizontal: "right",
      }}
      id={mobileMenuId}
      keepMounted
      transformOrigin={{
        vertical: "top",
        horizontal: "right",
      }}
      open={isMobileMenuOpen}
      onClose={handleMobileMenuClose}
    >
      <MenuItem>
        <IconButton onClick={(e) => { navigateToChatRoom(e) }} size="large" aria-label={`show chats`} color="inherit">
          <Badge color="error">
            <ChatIcon />
          </Badge>
        </IconButton>
        <p>chats</p>
      </MenuItem>
      <MenuItem>
        <IconButton size="large" aria-label={`show ${messagesCount} new messages`} color="inherit">
          <Badge badgeContent={messagesCount} color="error">
            <MailIcon />
          </Badge>
        </IconButton>
        <Typography>Messages</Typography>
      </MenuItem>
      <MenuItem>
        <IconButton
          size="large"
          aria-label={`show ${favouritesCount} favourites`}
          color="inherit"
        >
          <Badge badgeContent={favouritesCount} color="error">
            <StarOutlineIcon />
          </Badge>
        </IconButton>
        <Typography>Favourites</Typography>
      </MenuItem>
      <MenuItem onClick={handleProfileMenuOpen}>
        <IconButton
          size="large"
          aria-label="account of current user"
          aria-controls="primary-search-account-menu"
          aria-haspopup="true"
          color="inherit"
        >
          <AccountCircle />
        </IconButton>
        <Typography>Profile</Typography>
      </MenuItem>
    </Menu>
  );

  const [openFavList, setOpenFavList] = useState(false);
  const handleOpen = () => setOpenFavList(true);
  const handleClose = () => setOpenFavList(false);
  const favListStyle = {
    position: 'absolute',
    top: '5%',
    left: '28%',
    width: '44%',
    borderRadius: '10px',
    bgcolor: 'background.paper',
    border: '2px solid dimgray',
    boxShadow: 24,
    p: 4,
  };


  const handleTitleClick = (event) => {
    if (event) {
      event.preventDefault();
    }
    navigate('/dashboard');
  }
  return (
    <React.Fragment>
      <CssBaseline />
      <ElevationScroll {...props}>
        <ThemeProvider theme={navBarTheme}>
          <AppBar >
            <AppBar position="static" enableColorOnDark>
              <Toolbar>
                <Typography
                  variant="h6"
                  noWrap
                  component="div"
                  onClick={(e) => handleTitleClick(e)}
                  sx={{ display: { xs: "none", sm: "block", cursor: 'pointer' } }}>
                  {title}
                </Typography>
                <Search>
                  <SearchIconWrapper>
                    <SearchIcon />
                  </SearchIconWrapper>
                  <StyledInputBase
                    placeholder="Search…"
                    inputProps={{ "aria-label": "search" }}
                    onChange={(e) => { 
                      if (currentTab === 0) {
                        if (e.target.value.length == 0 || e.target.value.length > 2) {
                          dispatch(filterProperties({filterParams: {property_name: e.target.value}}));
                        }
                      } else {
                        if (e.target.value.length == 0 || e.target.value.length > 2) {
                          dispatch(filterServices({filterParams: {service_name: e.target.value}}));
                        }
                      }
                    }}
                  />
                </Search>
                <Box sx={{ flexGrow: 1 }} />
                <Box sx={{ display: { xs: "none", md: "flex" } }}>
                  <IconButton onClick={(e) => { navigateToChatRoom(e) }} size="large" aria-label={`show chats`} color="inherit">
                    <Badge color="error">
                      <ChatIcon />
                    </Badge>
                  </IconButton>

                  <IconButton
                    size="large"
                    aria-label={`show ${messagesCount} new messages`}
                    color="inherit"
                  >
                    <Badge badgeContent={messagesCount} color="error">
                      <MailIcon />
                    </Badge>
                  </IconButton>
                  <IconButton
                    size="large"
                    aria-label={`show ${favouritesCount} favourites`}
                    color="inherit"
                    onClick={handleOpen}>
                    <Badge badgeContent={favouritesCount} color="error">
                      <StarOutlineIcon />
                    </Badge>
                  </IconButton>
                  <Modal open={openFavList} onClose={handleClose} sx={{overflow:"auto"}}>
                    <Box sx={favListStyle}>
                      <FavList />
                    </Box>
                  </Modal>
                  <IconButton
                    size="large"
                    edge="end"
                    aria-label="account of current user"
                    aria-controls={menuId}
                    aria-haspopup="true"
                    onClick={handleProfileMenuOpen}
                    color="inherit"
                  >
                    <AccountCircle />
                  </IconButton>
                </Box>
                <Box sx={{ display: { xs: "flex", md: "none" } }}>
                  <IconButton
                    size="large"
                    aria-label="show more"
                    aria-controls={mobileMenuId}
                    aria-haspopup="true"
                    onClick={handleMobileMenuOpen}
                    color="inherit"
                  >
                    <MoreIcon />
                  </IconButton>
                </Box>
              </Toolbar>
            </AppBar>
            {/* {renderFilterMenu} */}
            {renderMobileMenu}
            {renderMenu}
          </AppBar>
        </ThemeProvider>
      </ElevationScroll>
      <Toolbar />
    </React.Fragment>
  );
}
