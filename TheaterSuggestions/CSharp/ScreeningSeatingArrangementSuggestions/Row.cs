namespace SeatsSuggestions;

public class Row
{
    private readonly string _name;
    private readonly List<SeatingPlace> _seatingPlaces;

    public Row(string name, List<SeatingPlace> seatingPlaces)
    {
        _name = name;
        _seatingPlaces = seatingPlaces;
    }

    public IEnumerable<SeatingPlace> SeatingPlaces => _seatingPlaces;
}
