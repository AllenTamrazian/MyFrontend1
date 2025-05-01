const BASE_URL = process.env.NEXT_PUBLIC_API_BASE_URL || 'http://localhost:3000';; // Your API base URL

// GET Request - Fetch data
async function getData(endpoint) {
    try {
        const response = await fetch(`${BASE_URL}/${endpoint}`);

        if (!response.ok) throw new Error(`GET Error: ${response.status}`);

        const data = await response.json();
        console.log('GET Response:', data);
        return data;
    } catch (error) {
        console.error('GET Request Failed:', error);
    }
}

// POST Request - Create data
async function postData(endpoint, payload) {
    try {
        const response = await fetch(`${BASE_URL}/${endpoint}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(payload),
        });

        if (!response.ok) throw new Error(`POST Error: ${response.status}`);

        const data = await response.json();
        console.log('POST Response:', data);
        return data;
    } catch (error) {
        console.error('POST Request Failed:', error);
    }
}

// PUT Request - Update data
async function putData(endpoint, payload) {
    try {
        const response = await fetch(`${BASE_URL}/${endpoint}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(payload),
        });

        if (!response.ok) throw new Error(`PUT Error: ${response.status}`);

        const data = await response.json();
        console.log('PUT Response:', data);
        return data;
    } catch (error) {
        console.error('PUT Request Failed:', error);
    }
}

// DELETE Request - Remove data
async function deleteData(endpoint) {
    try {
        const response = await fetch(`${BASE_URL}/${endpoint}`, {
            method: 'DELETE',
        });

        if (!response.ok) throw new Error(`DELETE Error: ${response.status}`);

        console.log('DELETE Successful');
    } catch (error) {
        console.error('DELETE Request Failed:', error);
    }
}
