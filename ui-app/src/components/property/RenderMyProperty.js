import { Avatar, Button, Card, CardActions, CardContent, CardHeader, CardMedia, Grid, IconButton, Typography } from "@mui/material";
import *  as React from "react";
import { AddFavorite } from "../Icons";
import EditProperty from "./EditProperty";
import Property from "./Property";
import {useEffect, useState} from "react";
import axios from "axios";
import { FAVORITE_PROPERTY } from "../../constants/Api";

export const RenderMyProperty = ({ property,handleDeleted,handleUpdate }) => {
    const blobData= property.images[0]?.image_data ;
    const [dialogOpened, setDialogOpened] = React.useState(false);
    const [editDialogOpened, setEditDialogOpened] = React.useState(false);
    const imageSrc = blobData ? `data:image/jpeg;base64,${blobData}` : ''

    const date = property?.posted_on
    const postedDate = date ? date.split('T')[0] : ''

    const [favP, setFavP]=useState(false);
    const [btnColor, setBtnColor] = useState("grey");
    const [favPId, setFavPId] = useState(0);

    const InitFavState = () => {
        axios.get(FAVORITE_PROPERTY + localStorage.getItem("userId"))
            .then((response) => {
                // console.log(response.data);
                let favPArr = response.data;
                favPArr.map((item) => {
                    if (item.property_id === property.property_id) {
                        // console.log("initColor"+btnColor);
                        setBtnColor("red");
                        setFavPId(item.favorite_property_id);
                        setFavP(true);
                    }
                })
            })
    }

    const ToggleFavP=(favPId)=>{
        setFavP((favP) => {
            if (favP === true && favPId!==0) {
                axios.delete(FAVORITE_PROPERTY+"delete/"+favPId)
                    .then((response) => {
                        // console.log(response.data);
                        setBtnColor("grey");
                        setFavP(false);
                    })
                    .catch(err => {
                        console.log(err);
                    });
            }
            if (favP === false) {
                axios.post(FAVORITE_PROPERTY+"add", {
                    user_id: localStorage.getItem("userId"),
                    property_id: property.property_id
                })
                    .then((response) => {
                        // console.log(response.data);
                        setBtnColor("red");
                        setFavP(true);
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
        {dialogOpened?<Property
        open={dialogOpened}
        onClose={setDialogOpened}
        setDialogOpenState={setDialogOpened}
        property = {property}
      />:<></>}
      {editDialogOpened?
          <EditProperty
          open={editDialogOpened}
          onClose={setEditDialogOpened}
          setDialogOpenState={setEditDialogOpened}
          property = {property}
          handleDeleted={handleDeleted}
          handleUpdate={handleUpdate}
        />:<></>}
      <Grid item xs={4}>
       <Card style={style.ServiceFeed} sx={{ maxWidth: 345 }}>
      <CardHeader
        avatar={
          <Avatar sx={{ bgcolor: "red" }} aria-label="recipe" />
        }
        title={property.user_name}
        action={
          <Button onClick={() => {
             setEditDialogOpened(true)
          }}>
          Edit
        </Button>
        }
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
                            ToggleFavP(favPId);
                        }}>
          <AddFavorite />
        </IconButton>
              </CardActions>
              <CardContent style={{paddingTop:'1%'}}>
                <div style={{justifyContent:'space-between',display:'flex',flexDirection:'row'}}>
                  <Typography fontSize={24}  fontWeight='bold'>{property.property_name}</Typography>
                  <Typography fontSize={24}  fontWeight='bold'>${property.rent}</Typography>
                </div>
                <Typography >{"Amenities: "+property.amenities}</Typography>
                <Typography >{"Bathrooms: "+property.bathrooms}</Typography>
                <Typography >{"Bedrooms: "+property.bedrooms}</Typography>
                <Typography >Parking: {property.parking_included?"Available":"Not available"}</Typography>
  
                <Typography marginTop={2.5} fontSize={16}>{property.address.location + " ,"+ property.address.postal_code}</Typography>
  
                <Typography fontSize={16}>{property.address.city + ",  "
                    + property.address.province + ", " +  property.address.country} </Typography>
              </CardContent>
              <CardActions>
                <Button style={{borderRadius:'20px'}} variant='contained' onClick={()=>{console.log("fdas"); setDialogOpened(true)}}
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