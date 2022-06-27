import { createAsyncThunk } from '@reduxjs/toolkit';
import axios from 'axios';
import { AUTH_USER, FILTER_PROPERTY, FILTER_SERVICE, GET_PROPERTY, GET_SERVICE, POST_PROPERTY,POST_SERVICE } from '../../../constants/Api';
export const authenticateUserData = createAsyncThunk(
  'app/login',
  async (payload) => {
    console.log('arg1 test: ', payload.email, payload.password);
    const response = await axios
      .post(AUTH_USER,  {
        email: payload.email,
        password: payload.password,
      },
      {
        headers: {
          "Accept": "application/json",
          "Content-Type": "application/json",
        }
      })

    return response.data;
  }
);

export const filterProperties = createAsyncThunk(
  'app/filter-properties',
  async (payload) => {
    const response = await axios
      .post(FILTER_PROPERTY,  {
        ...payload.filterParams
      },
      {
        headers: {
          "Accept": "application/json",
          "Content-Type": "application/json",
        }
      })

    return response.data;
  }
);

export const filterServices = createAsyncThunk(
  'app/filter-services',
  async (payload) => {
    const response = await axios
      .post(FILTER_SERVICE,  {
        ...payload.filterParams
      },
      {
        headers: {
          "Accept": "application/json",
          "Content-Type": "application/json",
        }
      })

    return response.data;
  }
);

export const getProperties = createAsyncThunk(
  'app/get-properties',
  async () => {
    const response = await axios
    .get(GET_PROPERTY);

    return response.data;
  }
);


export const getServices = createAsyncThunk(
  'app/get-services',
  async () => {
    const response = await axios
    .get(GET_SERVICE);

    return response.data;
  }
);

export const postService = createAsyncThunk(
  'app/post-service',
  async (payload) => {
    // const response = await axios({
    //   method: 'post',
    //   url: POST_PROPERTY,
    //   data: JSON.stringify({...payload.property}),
    //   headers: {
    //     'Content-Type': 'application/json'
    //   }
    // })

    const response = await axios
  .post(POST_SERVICE,  {
    ...payload.service},
    {
      headers: {
        "Accept": "application/json",
        "Content-Type": "application/json",
      }
    })

  return response.data;
}
);

export const postProperty = createAsyncThunk(
  'app/post-property',
  async (payload) => {
    // const response = await axios({
    //   method: 'post',
    //   url: POST_PROPERTY,
    //   data: JSON.stringify({...payload.property}),
    //   headers: {
    //     'Content-Type': 'application/json'
    //   }
    // })

    const response = await axios
      
      .post(POST_PROPERTY,  {
        ...payload.property
      },
      {
        headers: {
          "Accept": "application/json",
          "Content-Type": "application/json",
        }
      })

    return response.data;
  }
);
