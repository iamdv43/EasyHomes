import { Container, Grid, TextField } from "@mui/material";
import *  as React from "react";
import Typography from '@mui/material/Typography';
import Button from '@mui/material/Button';
import DialogTitle from '@mui/material/DialogTitle';
import Dialog from '@mui/material/Dialog';
import InputLabel from '@mui/material/InputLabel';
import MenuItem from '@mui/material/MenuItem';
import FormControl from '@mui/material/FormControl';
import Select from '@mui/material/Select';  
import Checkbox from '@mui/material/Checkbox';
import FormGroup from '@mui/material/FormGroup';
import FormControlLabel from '@mui/material/FormControlLabel';
import OutlinedInput from '@mui/material/OutlinedInput';
import InputAdornment from '@mui/material/InputAdornment';
import FormLabel from '@mui/material/FormLabel';
import axios from 'axios';
import Snackbar from '@mui/material/Snackbar';
import MuiAlert from '@mui/material/Alert';
import { POST_PROPERTY } from "../../constants/Api";
import { useDispatch } from 'react-redux';
import { postProperty } from '../../reducers/app/thunks/appThunk';

const Alert = React.forwardRef(function Alert(props, ref) {
  return <MuiAlert elevation={6} ref={ref} variant="filled" {...props} />;
});


export default function SimpleDialog(props) {
    const { open, title, setDialogOpenState, setToastMessage, setToastContent, resetPropertyForm } = props;
    const [propertyName, setPropertyName] = React.useState('');
    const [propertyType, setPropertyType] = React.useState('');
    const [bedrooms, setBedrooms] = React.useState('');
    const [bathrooms, setBathrooms] = React.useState('');
    const [rent, setRentChange] = React.useState('');
    const [propertyLocation, setPropertyLocation ] = React.useState('');
    const [city, setCity ] = React.useState('');
    const [province, setProvince ] = React.useState('');
    const [country, setCountry ] = React.useState('');
    const [postalCode, setPostalCode ] = React.useState('');
    const [base64Images, setbase64Images] = React.useState([]);
    const [snackbar, setSnackBar] = React.useState(false);
    const [severity, setSeverity] = React.useState('success');

    const dispatch = useDispatch();
    const images = [];

    const handleSnackClose = (event, reason) => {
      if (reason === 'clickaway') {
        return;
      }
  
      setSnackBar(false);
    };
  

    const initialValues ={
      id:0,
      propertyName:'',
      propertyType:'',
      address:{
        location:'',
        city:'',
        province:'',
        country:'',
        postal_code:''
      },
      amenities:'',
      bedrooms:0,
      bathrooms:0,
      parkingInclude:false,
      rent:0.0,
      images:[]
  }
  
    const onFileChange = (e) => {
      for (var i = 0; i < e.target.files.length; i++) {
        console.log(e.target.files[i]);
          if (e.target.files[i]) {
            const reader = new FileReader();
            reader.onload = _handleReaderLoaded.bind(this);
            reader.readAsBinaryString(e.target.files[i]);
            console.log('file uploaded: ', e.target.files[i]);
          }
      }       
    };
  
    const _handleReaderLoaded = e => {
      let binaryString = e.target.result;
      const base64Image = {
        image_data: btoa(binaryString)
      }
      images.push(base64Image)
      setbase64Images(images);
    };

    const [state, setState] = React.useState({
      laundry: true,
      wifi: false,
    });

    React.useEffect(() => {
      resetForm();
    }, [resetPropertyForm]);
  
    const handleChange = (event) => {
      setState({
        ...state,
        [event.target.name]: event.target.checked,
      });
    };
    
  
    const { laundry, wifi} = state;

    const [parkingIncluded, setParkingIncluded] = React.useState(true);

    const handlsetParkingIncludedeChange = (event) => {
        setParkingIncluded(event.target.checked);
    };


    const handleRentChange = (event) => {
      setRentChange(event.target.value);
    };

    const handlePropertyLocationChange = (event) => {
      setPropertyLocation(event.target.value);
    };

    const handleCityChange = (event) => {
      setCity(event.target.value);
    };

    const handleProvinceChange = (event) => {
      setProvince(event.target.value);
    };

    const handleCountryChange = (event) => {
      setCountry(event.target.value);
    };

    const handlePostalCodeChange = (event) => {
      setPostalCode(event.target.value);
    };
    const handleClose = () => {
        setDialogOpenState();
      };

      const handlePropertyTypeChange = (event) => {
        setPropertyType(event.target.value);
      };

      const handleBedroomsChange = (event) => {
        setBedrooms(event.target.value);
      };
      const handleBathroomsChange = (event) => {
        setBathrooms(event.target.value);
        console.log(initialValues.bathrooms);
      };

      const handlePropertyNameChange = (event) => {
        setPropertyName(event.target.value);
      };

      const disablePostButton = () => {
        return (propertyName === null || propertyName === ''|| propertyType === null || propertyType === ''
        ||propertyLocation === null || propertyLocation === ''|| city === null || city === ''|| province == null || 
        province === ''|| country === null || country === '' || postalCode  === null || postalCode  ==='' )
      };
    

      const submitPropertyPost = (initialValues) => {
       const property = {
         "user_id": localStorage.getItem("userId"),
          "property_name": propertyName,
          "address":{
            "location" : propertyLocation,
            "city": city,
            "province":province,
            "country": country,
            "postal_code": postalCode
          },

          "amenities": "" + (laundry ? "Laundry" : "") + "," +(wifi ? "Wifi" : ""),
          "property_type":propertyType,
          "bathrooms": bathrooms,
          "bedrooms": bedrooms,
          "parking_included":parkingIncluded,
          "rent": rent,
          "images": [...base64Images]
        };
      // JSON.stringify(property)
        dispatch(postProperty({
          property: property
        }));
      // axios({
      //   method: 'post',
      //   url: POST_PROPERTY,
      //   data: JSON.stringify(property),
      //   headers: {
      //     'Content-Type': 'application/json'
      //   }
      //   })
      //   .then(function (response) {
      //       setSnackBar(true);
      //       setSeverity("success");
      //       setDialogOpenState(false);
      //       resetForm();
      //       setToastMessage(true);
      //       setToastContent("Property added successfully!");
      //       console.log(snackbar);
      //   })
      //   .catch(function (response) {
      //       console.log(response);
      //   });
      };

      const handleDialogClose = () => {
        setDialogOpenState();
      }
      const resetForm = () => {
       setPropertyName('');
        setPropertyType('');
        setBedrooms('');
        setBathrooms('');
        setRentChange('');
        setPropertyLocation('');
        setCity('');
        setProvince('');
        setCountry('');
        setPostalCode('');
        setbase64Images([]);
      }  

      // if (resetPropertyForm) {
      //   // check if the form is reset
      //   if (!(propertyName === '' && propertyType === '' && bedrooms === '' 
      //     && bathrooms === '' && rent === '' && propertyLocation === ''
      //     && city === '' && province === '' && country === '' 
      //     && postalCode === '' && base64Images.length === 0)) {
      //     resetForm();
      //   }
      // }     
    return( <Dialog onBackdropClick={handleDialogClose} onClose={handleClose} open={open}>
        <DialogTitle>{title}</DialogTitle>
        <form>
        <Container  maxWidth="md">
          <Grid container spacing={2}>
            <Grid item xs={6}>
            <TextField
                required
                id="propertyName"
                name="propertyName"
                label="Property Name"
                value={propertyName}
                onChange={handlePropertyNameChange}
                fullWidth
                size="small"
              />
            </Grid>
            <Grid item xs={6}>
              <FormControl fullWidth>
                <InputLabel id="add-property-dialog-property-type">
                  Property Type
                </InputLabel>
                <Select
                labelId="add-property-dialog-property-type-label"
                id="add-property-dialog-property-type-id"
                label="Property Type"
                value={propertyType}
                onChange={handlePropertyTypeChange}
                size="small"
                >
                <MenuItem value={"1 BHK"}>1 BHK</MenuItem>
                <MenuItem value={"2 BHK"}>2 BHK</MenuItem>
                <MenuItem value={"3 BHK"}>3 BHK</MenuItem>
                <MenuItem value={"4 BHK"}>4 BHK</MenuItem>
                <MenuItem value={"Townhouse"}>TownHouse</MenuItem>
                </Select>
              </FormControl>
            </Grid>
            <Grid item xs={6}>
              <FormControl fullWidth>
                <InputLabel id="add-property-dialog-bedrooms">
                  Bedrooms
                </InputLabel>
                <Select
                labelId="add-property-dialog-bedrooms-label"
                id="add-property-dialog-bedrooms-id"
                label="Bedrooms"
                value={bedrooms}
                onChange={handleBedroomsChange}
                size="small"
                >
                <MenuItem value={1}>One</MenuItem>
                <MenuItem value={2}>Two</MenuItem>
                <MenuItem value={3}>Three</MenuItem>
                </Select>
              </FormControl>
            </Grid>
            <Grid item xs={6}>
              <FormControl fullWidth>
                <InputLabel id="add-property-dialog-bathrooms">
                  Bathrooms
                </InputLabel>
                <Select
                labelId="add-property-dialog-bathrooms-label"
                id="add-property-dialog-bedrooms-id"
                label="Bathrooms"
                value={bathrooms}
                onChange={handleBathroomsChange}
                size="small"
                >
                <MenuItem value={1}>One</MenuItem>
                <MenuItem value={2}>Two</MenuItem>
                <MenuItem value={3}>Three</MenuItem>
                </Select>
              </FormControl>
            </Grid>
            <Grid item xs ={6}>
              <FormGroup>
                <FormControlLabel control={<Checkbox   
                  checked={parkingIncluded}
                  onChange={handlsetParkingIncludedeChange} />} label="Parking Included" />
              </FormGroup>
            </Grid>
            <Grid item xs>
            <FormControl fullWidth sx>
              <InputLabel htmlFor="post-property-dailog-rent">Rent</InputLabel>
              <OutlinedInput
                id="post-property-dailog-rent"
                value={rent}
                onChange={handleRentChange}
                startAdornment={<InputAdornment position="start">$</InputAdornment>}
                label="Rent"
                size="small"
              />
            </FormControl>
            </Grid>
            <Grid item xs={6}>
            <FormControl component="fieldset" variant="standard">
            <FormLabel component="legend">Amenities</FormLabel>
            <FormGroup>
              <FormControlLabel
                control={
                  <Checkbox checked={laundry} onChange={handleChange} name="laundry" />
                }
                label="Laundry Services"
              />
              <FormControlLabel
                control={
                  <Checkbox checked={wifi} onChange={handleChange} name="wifi" />
                }
                label="Free Wifi"
              />
            </FormGroup>
          </FormControl>
            </Grid>
          </Grid>
        </Container>
        <div style={{ padding: 20 }}></div>
        <Container  maxWidth="md">
          <Grid item xs={6}> <Typography variant="h6" gutterBottom component="div">
          Property Address
          </Typography>
          </Grid>
          <Grid container spacing={2}>
          <Grid item xs={6}>
            <TextField
                required
                id="propertyLocation"
                name="propertyLocation"
                label="Property Location"
                size="small"
                value={propertyLocation}
                onChange={handlePropertyLocationChange}
                fullWidth
              />
          </Grid>
          <Grid item xs={6}>
            <TextField
                required
                id="city"
                name="city"
                label="City"
                size="small"
                value={city}
                onChange={handleCityChange}
                fullWidth
              />
          </Grid>
          <Grid item xs={6}>
            <TextField
                required
                id="province"
                name="province"
                label="Province"
                size="small"
                value={province}
                onChange={handleProvinceChange}
                fullWidth
              />
          </Grid>
          <Grid item xs={6}>
            <TextField
                required
                id="country"
                name="country"
                label="Country"
                size="small"
                value={country}
                onChange={handleCountryChange}
                fullWidth
              />
          </Grid>
          <Grid item xs={6}>
            <TextField
                required
                id="postalCode"
                name="postalCode"
                label="Postal Code"
                size="small"
                value={postalCode}
                onChange={handlePostalCodeChange}
                fullWidth
              />
          </Grid>
      </Grid>
      </Container>
      <Container  maxWidth="md">
      <div style={{ padding: 20 }}></div>  
      <Grid item xs={6}> <Typography variant="h6" gutterBottom component="div">
          Upload Images
      </Typography>
      </Grid>
      <Grid item xs={6}>
      <label htmlFor="icon-button-file">
      <input
          type="file"
          name="image"
          id="file"
          accept=".jpg, .jpeg, .png"
          multiple
          onChange={onFileChange}
        />
      </label>
      </Grid>
      <div style={{ padding: 20 }}></div>
      <Button variant="contained" disabled={disablePostButton()} onClick={submitPropertyPost}>Post</Button>
      {/* <Snackbar open={snackbar} autoHideDuration={6000} onClose={handleSnackClose}>
          <Alert onClose={handleSnackClose} severity={severity} sx={{ width: '100%' }}>
            This is a success message!
          </Alert>
        </Snackbar> */}
      </Container>
      </form>
    

      <div style={{ padding: 20 }}></div>
     
      </Dialog>);
 }