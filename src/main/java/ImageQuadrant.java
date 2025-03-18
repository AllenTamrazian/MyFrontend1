package viperws;

import java.util.List;

/**
 * Represents an image quadrant with an ID, associated labels, and rock count.
 */
public class ImageQuadrant {
    private int id;
    private String imageURL;
    private List<String> labels;
    private int rockCount;  // New: Track number of rocks

    public ImageQuadrant(int id, String imageURL, List<String> labels, int rockCount) {
        this.id = id;
        this.imageURL = imageURL;
        this.labels = labels;
        this.rockCount = rockCount;
    }

    public int getId() {
        return id;
    }

    public String getImageURL() {
        return imageURL;
    }

    public List<String> getLabels() {
        return labels;
    }

    public int getRockCount() {
        return rockCount;
    }

    public void addLabel(String label) {
        labels.add(label);
    }

    public void incrementRockCount() {
        rockCount++;
    }

    public void decrementRockCount() {
        if (rockCount > 0) {
            rockCount--;
        }
    }

    public void setRockCount(int count) {
        if (count >= 0) {
            this.rockCount = count;
        }
    }
}
