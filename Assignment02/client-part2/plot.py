import pandas as pd
import matplotlib.pyplot as plt
import sys

def generate_throughput_plot(csv_file, output_file='throughput_plot.png'):
    print(f"Reading data from {csv_file}...")
    df = pd.read_csv(csv_file)

    print(f"Total records: {len(df)}")

    min_time = df['start_time'].min()
    df['second'] = (df['start_time'] - min_time) // 1000

    throughput = df.groupby('second').size()

    # Create plot
    plt.figure(figsize=(12, 6))
    plt.plot(throughput.index, throughput.values, linewidth=1, color='black')

    # Add labels and title
    plt.xlabel('Time (seconds)', fontsize=12)
    plt.ylabel('Throughput (req/s)', fontsize=12)
    plt.title('Client Part 2: Instantaneous Throughput Over Time\n(100 threads, 200,000 requests)', fontsize=14)

    # Add grid for better readability
    plt.grid(True, alpha=0.3)

    # Add statistics annotation
    avg_throughput = throughput.mean()
    max_throughput = throughput.max()
    min_throughput = throughput.min()

    stats_text = f'Avg: {avg_throughput:.0f} req/s\nMax: {max_throughput:.0f} req/s\nMin: {min_throughput:.0f} req/s'
    plt.annotate(stats_text, xy=(0.02, 0.98), xycoords='axes fraction',
                 verticalalignment='top', fontsize=10,
                 bbox=dict(boxstyle='round', facecolor='wheat', alpha=0.5))

    # Save and display plot
    plt.tight_layout()
    plt.savefig(output_file, dpi=150)
    print(f"Plot saved to {output_file}")

    # Print summary
    print("THROUGHPUT STATISTICS")
    print("=" * 50)
    print(f"Average throughput: {avg_throughput:.2f} req/s")
    print(f"Maximum throughput: {max_throughput} req/s")
    print(f"Minimum throughput: {min_throughput} req/s")
    print(f"Test duration: {throughput.index.max()} seconds")
    print("=" * 50)

    plt.show()

if __name__ == '__main__':
    if len(sys.argv) < 2:
        print("Usage: python plot_throughput.py <csv_file> [output_png]")
        sys.exit(1)

    csv_file = sys.argv[1]
    output_file = sys.argv[2] if len(sys.argv) > 2 else 'throughput_plot.png'

    generate_throughput_plot(csv_file, output_file)