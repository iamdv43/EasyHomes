import React, {useState} from 'react'

import Avatar from "@mui/material/Avatar";
import Button from "@mui/material/Button";
import CssBaseline from "@mui/material/CssBaseline";
import TextField from "@mui/material/TextField";
import Link from "@mui/material/Link";
import Grid from "@mui/material/Grid";
import Box from "@mui/material/Box";
import LockOutlinedIcon from "@mui/icons-material/LockOutlined";
import Typography from "@mui/material/Typography";
import Container from "@mui/material/Container";
import { createTheme, ThemeProvider } from "@mui/material/styles";

import { useNavigate } from "react-router-dom";

import axios from "axios";
import { NEW_PASSWORD } from '../constants/Api';


const theme = createTheme();


const NewPassword = ({email})  =>{

    const [password, setpassword] = useState('');

    const [confirmpassword, setRepassword] = useState('');

    const navigate = useNavigate();

    const savePassword = (event) => {
        event.preventDefault();
    
        if (password !== confirmpassword) {
                alert("Passwords don't match");
            } else {
                
        console.log(email);
        axios
        .post(
          NEW_PASSWORD,
          {email: email, password: password },
          {
            headers: {
              Accept: "application/json",
              "Content-Type": "application/json",
            },
          }
        )
        .then((response) => {
          console.log(response.data);
          navigate("/login");
        })
        .catch(function (error) {
          if (error.response) {
            alert(error.response.data);
          }
        });
            }
        };
      

    return (
        <ThemeProvider theme={theme}>
        <Container component="main" maxWidth="xs">
          <CssBaseline />
          <Box
            sx={{
              marginTop: 8,
              display: "flex",
              flexDirection: "column",
              alignItems: "center",
            }}
          >
            <Avatar sx={{ m: 1, bgcolor: "secondary.main" }}>
              <LockOutlinedIcon />
            </Avatar>
            <Typography component="h1" variant="h5">
              Enter New Password
            </Typography>
            <Box component="form" noValidate sx={{ mt: 1 }}>
              <TextField
                margin="normal"
                required
                fullWidth
                id="password"
                name="password"
                type="password"
                value={password}
                onChange={(e) => setpassword(e.target.value)}
                label="Enter New Password"
                autoFocus
              />

              <TextField
                margin="normal"
                required
                fullWidth
                id="confirmpassword"
                name="confirmpassword"
                type="password"
                value={confirmpassword}
                onChange={(e) => setRepassword(e.target.value)}
                label="Confirm Password"
                autoFocus
              />
  
              <Button
                type="submit"
                fullWidth
                variant="contained"
                disabled={password.length === 0 }
                sx={{ mt: 3, mb: 2 }}
                onClick={savePassword} 
              >
                Submit
              </Button>
              <Grid container>
                <Grid item xs>
                  {
                    <Link href="login" variant="body2">
                      {"Login"}
                    </Link>
                  }
                </Grid>
                <Grid item>
                  <Link href="register" variant="body2">
                    {"Don't have an account? Sign Up"}
                  </Link>
                </Grid>
              </Grid>
            </Box>
          </Box>
        </Container>
      </ThemeProvider>        
    );
}


export default NewPassword;