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
import { OTP_VERIFICATION } from '../constants/Api';
import axios from "axios";


const theme = createTheme();


const OTPpopup = ({setStage})  =>{

    const [otp, setOTP] = useState("");

    const saveUser = (event) => {
        event.preventDefault();
    
        axios
          .post(
            OTP_VERIFICATION,
            { otp: otp },
            {
              headers: {
                Accept: "application/json",
                "Content-Type": "application/json",
              },
            }
          )
          .then((response) => {
            console.log(response.data);
            setStage(3);
          })
          .catch(function (error) {
            if (error.response) {
              alert(error.response.data);
            }
          });
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
              Enter OTP
            </Typography>
            <Box component="form" noValidate sx={{ mt: 1 }}>
              <TextField
                margin="normal"
                required
                fullWidth
                id="otp"
                name="otp"
                value={otp}
                onChange={(e) => setOTP(e.target.value)}
                label="Enter OTP"
                autoFocus
              />
  
              <Button
                type="submit"
                fullWidth
                variant="contained"
                disabled={otp.length === 0}
                sx={{ mt: 3, mb: 2 }}
                onClick={saveUser} 
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




export default OTPpopup;