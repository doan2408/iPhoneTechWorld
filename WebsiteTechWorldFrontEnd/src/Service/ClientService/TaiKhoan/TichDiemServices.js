import api from "@/Service/LoginService/axiosInstance";

const baseUrl = "/client/lichSuDiem"

export const getLichSuDiem = async({
    fromDate = null,
    toDate = null,
    page = 0,
    size =10
}) => {
    try {
        const params = { page, size }
        if(fromDate != null) params.fromDate = fromDate
        if(toDate !=null) params.toDate = toDate;

        const response = await api.get(`${baseUrl}`, {params});
        return response.data;

    }
    catch (err) {
        console.error("An error was thrown while loading the point of client", err);
        throw err.response?.data || "Error getting points";
    };
};