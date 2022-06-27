import { Avatar, Button, Card, CardActions, CardContent, CardHeader, CardMedia, Chip, Grid, IconButton, Stack, Typography } from "@mui/material";
import *  as React from "react";
import { AddFavorite } from "../Icons";
import EditService from "./EditService";
import Service from "./Service";
import {useEffect, useState} from "react";
import axios from "axios";
import { FAVORITE_SERVICE } from "../../constants/Api";

export const RenderMyService = ({ service,handleServiceUpdate,handleServiceDeleted }) => {
    const blobData= service.images[0]?.image_data ;
    const [dialogOpened, setDialogOpened] = React.useState(false);
    const [editDialogOpened, setEditDialogOpened] = React.useState(false);
    const imageSrc = blobData ? `data:image/jpeg;base64,${blobData}` : ''

    const date = service?.posted_on
    const postedDate = date ? date.split('T')[0] : ''

    const [favS, setFavS]=useState(false);
    const [btnColor, setBtnColor] = useState("grey");
    const [favSId, setFavSId] = useState(0);

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

    return (
        <>
        {dialogOpened?<Service
        open={dialogOpened}
        onClose={setDialogOpened}
        setDialogOpenState={setDialogOpened}
        service = {service}
      />:<></>}
      {editDialogOpened?
          <EditService
          open={editDialogOpened}
          onClose={setEditDialogOpened}
          setDialogOpenState={setEditDialogOpened}
          service = {service}
          handleServiceUpdate={handleServiceUpdate}
          handleServiceDeleted={handleServiceDeleted}
        />:<></>}
        <Grid item xs={4}>
        <Card style={style.ServiceFeed} sx={{ maxWidth: 345 }}>
          <CardHeader
            avatar={
              <Avatar sx={{ bgcolor: "red" }} aria-label="recipe" />
            }
            title={service.user_name}
            action={
                <Button onClick={() => {
                   setEditDialogOpened(true)
                }}>
                Edit
              </Button>}
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