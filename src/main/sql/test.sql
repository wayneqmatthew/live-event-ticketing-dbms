SELECT
    e.event_id,
    e.event_name,
    COUNT(t.ticket_id) AS TicketsSold,
    SUM(t.price) AS TotalRevenue
FROM
    Event e
JOIN
    Ticket t ON e.event_id = t.event_id -- CORRECT JOIN condition
WHERE
    t.status = 'Active'
GROUP BY
    e.event_id, e.event_name; -- Only group by the event details