import React, {useState} from "react";
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
import { FORGOT_PASSWORD } from "../constants/Api";

import axios from "axios";

const theme = createTheme();

const EnterMail = ({ email, setEmail,  setStage  })  => {
 
  const [isValid, setIsValid] = useState(false);
  const [message, setMessage] = useState("");

  const emailRegex = /\S+@\S+\.\S+/;

  const validateEmail = (event) => {
    const email = event.target.value;
    if (emailRegex.test(email)) {
      setIsValid(true);
      setMessage("Your email looks good!");
    } else {
      setIsValid(false);
      setMessage("Please enter a valid email!");
    }
  };

  const saveUser = (event) => {
    event.preventDefault();

    axios
      .post(
        FORGOT_PASSWORD,
        { email: email },
        {
          headers: {
            Accept: "application/json",
            "Content-Type": "application/json",
          },
        }
      )
      .then((response) => {
        console.log(response.data);
        setStage(2);
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
            Forgot Password
          </Typography>
          <Box component="form" noValidate sx={{ mt: 1 }}>
            <TextField
              margin="normal"
              required
              fullWidth
              id="email"
              name="email"
              value={email}
              onChange={(e) => {setEmail(e.target.value); validateEmail(e)}}
              label="Email Address"
              autoComplete="email"
              autoFocus
            />
             <div className={`message ${isValid ? "success" : "error"}`}>
                  {message}
                </div>

            <Button
              type="submit"
              fullWidth
              variant="contained"
              disabled={email.length === 0 || !isValid}
              sx={{ mt: 3, mb: 2 }}
              onClick={saveUser} 
            >
              Send OTP
            </Button>
            <Grid container>
              <Grid item xs>
                {
                  <Link href="login" variant="body2">
                    {"Sign In"}
                  </Link>
                }
              </Grid>
              <Grid item>
                <Link href="register" variant="body2">
                  {"Sign Up"}
                </Link>
              </Grid>
            </Grid>
          </Box>
        </Box>
      </Container>
    </ThemeProvider>
  );
};

export default EnterMail;
