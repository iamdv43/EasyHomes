import { Avatar, Button, Card, CardActions, CardContent, CardHeader, CardMedia, Chip, Grid, IconButton, Stack, Typography } from "@mui/material";
import *  as React from "react";
import { AddFavorite } from "../Icons";
import Service from "./Service";
import {useEffect, useState} from "react";
import axios from "axios";
import { FAVORITE_SERVICE } from "../../constants/Api";
import { Container } from "@mui/material";
import { SERVICE_BOOK_APPOINTMENT } from "../../constants/Api";
import Popup from "../Popup";
import { TextField } from "@mui/material";
import DateTimePicker from "react-datetime-picker";
import {Dialog } from "@mui/material";

import ChatIcon from '@mui/icons-material/Chat';
import { generatePath, useNavigate } from 'react-router';

export const RenderService = ({ service, handlePost }) => {


    const blobData= service.images[0]?.image_data ;
    const [dialogOpened, setDialogOpened] = React.useState(false);
    const imageSrc = blobData ? `data:image/jpeg;base64,${blobData}` : ''

    const date = service?.posted_on
    const postedDate = date ? date.split('T')[0] : ''

    const [favS, setFavS]=useState(false);
    const [btnColor, setBtnColor] = useState("grey");
    const [favSId, setFavSId] = useState(0);

    const navigate = useNavigate();
    const navigateToChatRoom = (event, service) => {
      if (event) {
        event.preventDefault();
      }
      navigate('/chat-room', { state: { chatPeerInfo: service.user_name} });
    }

    const InitFavState = () => {
        axios.get(FAVORITE_SERVICE + localStorage.getItem("userId"))
            .then((response) => {
                // console.log(response.data);
                let favSArr = response.data;
                favSArr.map((item) => {
                    if (item.service_id === service.service_id) {
                        setBtnColor("red");
                        setFavSId(item.favorite_service_id);
                        setFavS(true);
                    }
                })
            })
    }

    const ToggleFavS=(favSId)=>{
        setFavS((favS) => {
            if (favS === true && favSId!==0) {
                axios.delete(FAVORITE_SERVICE+"delete/"+favSId)
                    .then((response) => {
                        setBtnColor("grey");
                        setFavS(false);
                    })
                    .catch(err => {
                        console.log(err);
                    });
            }
            if (favS === false) {
                axios.post(FAVORITE_SERVICE+"add", {
                    user_id: localStorage.getItem("userId"),
                    service_id: service.service_id
                })
                    .then((response) => {
                        console.log(response.data);
                        setBtnColor("red");
                        setFavS(true);
                    })
                    .catch(err => {
                        console.log(err);
                    });
            }
        });
    }

    InitFavState();
  
    const [popIsOpen, setpopIsOpen] = useState(false);
    const [messageS, setMessageS] = useState("");
    const [dateandtimee, setDateandtimee] = useState(new Date());
  
    const togglePopupOpen = (e) => {
      setpopIsOpen(true);
    };
  
    const togglePopupClose = () => {
      setpopIsOpen(false);
    };
  
    const scheduleMeeting = (service, event) => {
      if (event) {
        event.preventDefault();
      }
  
      const scheduleMeetingDetails = {
        user_id: localStorage.getItem("userId"),
        service_id: service?.service_id.toString(),
        service_user_id: service?.user_id.toString(),
        message: messageS,
        appointmentTime: dateandtimee,
      };
  
      setpopIsOpen(false);
  
      console.log("Service details:  " + JSON.stringify(scheduleMeetingDetails));
  
      axios
        .post(SERVICE_BOOK_APPOINTMENT, JSON.stringify(scheduleMeetingDetails), {
          headers: {
            Accept: "application/json",
            "Content-Type": "application/json",
          },
        })
        .then((response) => {
          console.log(response.data);
          alert("Email sent.");
        })
        .catch(function (error) {
          if (error.response) {
            alert(error.response.data);
          }
        });
    };

    return (
      <>
      {dialogOpened?<Service
        open={dialogOpened}
        onClose={setDialogOpened}
        setDialogOpenState={setDialogOpened}
        service = {service}
      />:<></>}
      <Grid item xs={4}>
        <Card style={style.ServiceFeed} sx={{ maxWidth: 345 }}>
          <CardHeader
            avatar={
              <Avatar sx={{ bgcolor: "red" }} aria-label="recipe" />
            }
            title={service.user_name}
            subheader={postedDate}
          />
          <CardMedia
            component="img"
            height="194"
            image={imageSrc}
          />
          <CardActions disableSpacing style={{justifyContent:'space-between'}}>
          <IconButton aria-label="add to favorites"
                      style={{ color: btnColor}}
                      onClick={()=>{
                          ToggleFavS(favSId);
                      }}>
            <AddFavorite />
            </IconButton>
        </CardActions>
          <CardContent style={{paddingTop:'1%'}}>
          <div style={{justifyContent:'space-between',display:'flex',flexDirection:'row'}}>
          <Typography fontSize={24}  fontWeight='bold'>{service.service_name}</Typography>
          <Typography fontSize={24}  fontWeight='bold'>${service.cost}</Typography>
          </div>
            <Typography >{service.description>100?
              service.description.substring(0,100)+'...'
              :service.description}</Typography>

          <Typography fontSize={16}  fontWeight='bold'>Subscription</Typography>
            <Stack direction="row" spacing={1}>
            <Chip size="small" label={service.plan}/>
            </Stack>
            <Typography marginTop={2.5} fontSize={16}>{service.address + " ,"+ service.pincode}</Typography>

            <Typography fontSize={16}>{service.city + ",  "
            + service.province + ", " +  service.country} </Typography>
          </CardContent>
          <CardActions>
            <Button style={{borderRadius:'20px'}} variant='contained' onClick={()=>{setDialogOpened(true)}}
                        size="small">Learn More</Button>

<Button variant="contained"
              onClick={(e) => {
                togglePopupOpen(e);
                 }}
            >
              Book an Appointment
            </Button>
            {popIsOpen && (
              <Popup
                content={
                  <>
                    <Container maxWidth="md">
                      <Grid container spacing={2}>
                        <Grid item xs={6}>
                          <TextField
                            required
                            id="message"
                            name="messgae"
                            label="Enter a message"
                            value={messageS}
                            onChange={(e) => {
                              setMessageS(e.target.value);
                            }}
                            fullWidth
                          />
                        </Grid>
                        <Grid item xs={6}>
                          <DateTimePicker
                            onChange={setDateandtimee}
                            value={dateandtimee}
                          />
                        </Grid>
                      </Grid>
                      <div>
                        <Button
                          style={{
                            margin: "10px 10px 0px 10px",
                          }}
                          item
                          xs={6}
                          onClick={(e) => {
                            scheduleMeeting(service, e);
                          }}
                          disabled={messageS.length === 0}
                        >
                          Submit
                        </Button>
                      </div>
                      <div>
                        <Button
                          style={{
                            margin: "10px 10px 0px 10px",
                          }}
                          item
                          xs={6}
                          onClick={(e) => {
                            togglePopupClose(e);
                          }}
                        >
                          Close
                        </Button>
                      </div>
                    </Container>
                  </>
                }
              />
            )}
            <Button onClick={(e) => { navigateToChatRoom(e, service) }}
              size="small">Chat</Button>
        </CardActions>
        </Card>
      </Grid>
      </>
    );
  };

  const style = {
    ServiceFeed: {
      backgroundColor: "#F5F5F5",
      borderRadius: 25,
    },
  };